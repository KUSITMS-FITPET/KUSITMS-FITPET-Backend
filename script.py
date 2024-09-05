import jpype
import boto3
import sys
import urllib.parse

# JVM 시작
jpype.startJVM()

from asposecells.api import Workbook, FileFormatType, PdfSaveOptions

# S3 연결 함수
def s3_connection():
    try:
        s3 = boto3.client(
            service_name="s3",
            region_name="us-east-2",
            aws_access_key_id="AKIA2R3FKW6IGGAOQB7P",
            aws_secret_access_key="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",
        )
    except Exception as e:
        print(e)
    else:
        print("s3 bucket connected!")
        return s3

s3 = s3_connection()

# 엑셀 파일을 PDF로 변환하는 함수
def excel_to_pdf(excel_file, output_pdf):
    try:
        workbook = Workbook(excel_file)
        sheet = workbook.getWorksheets().get(0)
        saveOptions = PdfSaveOptions()
        saveOptions.setOnePagePerSheet(True)
        workbook.save(output_pdf, saveOptions)
        print(f"Excel file {excel_file} successfully converted to PDF {output_pdf}")
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

def download_from_s3(bucket_name, s3_key, download_path):
    try:
        s3.download_file(bucket_name, s3_key, download_path)
        print(f"File downloaded successfully from s3://{bucket_name}/{s3_key} to {download_path}")
    except Exception as e:
        print(f"Failed to download file from S3: {e}")

# ProcessBuilder에서 받은 명령행 인자를 처리
if __name__ == "__main__":
    if len(sys.argv) != 4:
        print("Usage: script.py <input_excel_file> <output_pdf_file>")
        sys.exit(1)

    input_excel = sys.argv[1]
    output_pdf = '/app/010-0902-9820_2024-09-02T20:59:45.752952.pdf'
    excelFileName = sys.argv[3]
    # PDF로 변환 및 S3 업로드 처리
    if s3:
        download_from_s3('fitpetbucket', excelFileName, input_excel)

        # 엑셀을 PDF로 변환
        excel_to_pdf(input_excel, output_pdf)

        # 변환된 PDF를 S3에 업로드
        upload_to_s3(output_pdf, 'fitpetbucket', 'example0905.pdf')
