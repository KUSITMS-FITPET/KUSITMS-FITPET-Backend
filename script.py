import jpype
import boto3
import sys
import urllib.parse

# JVM 시작
jpype.startJVM()

from asposecells.api import Workbook, FileFormatType, PdfSaveOptions, FontConfigs

# S3 연결 함수
def s3_connection():
    try:
        s3 = boto3.client(
            service_name="s3",
            region_name="us-east-2",
            aws_access_key_id="AKIA2R3FKW6IGGAOQB7P",
            aws_secret_access_key="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",
        )
        print("s3 bucket connected!")
        return s3
    except Exception as e:
        print(f"Failed to connect to S3: {e}")
        return None

s3 = s3_connection()

# 엑셀 파일의 특정 시트들만 포함하는 PDF로 변환하는 함수
def excel_to_pdf(excel_file, sheet_indices, output_pdf):
    try:

        font_folder = "/usr/share/fonts/truetype/nanum"  # 나눔 폰트가 설치된 경로
        FontConfigs.setFontFolder(font_folder, False)

        # 원본 워크북 로드
        workbook = Workbook(excel_file)

        # 새로운 워크북 생성
        new_workbook = Workbook()
        new_workbook.getWorksheets().clear()  # 기존 시트 삭제

        # 선택한 시트들을 새 워크북으로 복사
        for sheet_index in sheet_indices:
            if sheet_index < workbook.getWorksheets().getCount():
                original_sheet = workbook.getWorksheets().get(sheet_index)
                copied_sheet = new_workbook.getWorksheets().add(original_sheet.getName())
                copied_sheet.copy(original_sheet)

        # PDF 저장 옵션 설정
        saveOptions = PdfSaveOptions()
        saveOptions.setOnePagePerSheet(True)

        # 새로운 워크북을 PDF로 저장
        new_workbook.save(output_pdf, saveOptions)
        print(f"Selected sheets {sheet_indices} from {excel_file} successfully converted to PDF {output_pdf}")
    except Exception as e:
        print(f"Error in converting Excel to PDF: {e}")
    finally:
        jpype.shutdownJVM()

# S3에 파일 업로드 함수
def upload_to_s3(file_path, bucket_name, s3_key):
    try:
        s3.upload_file(file_path, bucket_name, s3_key)
        print(f"File uploaded successfully to s3://{bucket_name}/{s3_key}")
        s3_url = f"https://{bucket_name}.s3.amazonaws.com/{urllib.parse.quote(s3_key)}"
        print(f"s3_url: {s3_url}")
    except Exception as e:
        print(f"Failed to upload file to S3: {e}")

# S3에서 파일 다운로드 함수
def download_from_s3(bucket_name, s3_key, download_path):
    try:
        s3.download_file(bucket_name, s3_key, download_path)
        print(f"File downloaded successfully from s3://{bucket_name}/{s3_key} to {download_path}")
    except Exception as e:
        print(f"Failed to download file from S3: {e}")

# ProcessBuilder에서 받은 명령행 인자를 처리
if __name__ == "__main__":
    if len(sys.argv) != 4:
        print("Usage: script.py <input_excel_file> <output_pdf_file> <excel_file_key_in_s3>")
        sys.exit(1)

    input_excel = sys.argv[1]
    output_pdf = '/app/010-0902-9820_2024-09-02T20:59:45.752952.pdf'
    excelFileName = sys.argv[3]

    # PDF로 변환 및 S3 업로드 처리
    if s3:
        download_from_s3('fitpetbucket', excelFileName, input_excel)

        # 엑셀 파일의 시트 0, 1, 2를 PDF로 변환
        excel_to_pdf(input_excel, [0, 1, 2], output_pdf)

        # 변환된 PDF를 S3에 업로드
        upload_to_s3(output_pdf, 'fitpetbucket', 'example0905.pdf')

    # JVM 종료
    jpype.shutdownJVM()