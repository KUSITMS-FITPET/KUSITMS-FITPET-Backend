import jpype
import asposecells
import boto3

jpype.startJVM()
from asposecells.api import Workbook, FileFormatType, PdfSaveOptions

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

def excel_to_pdf(excel_file, output_pdf):
    workbook = Workbook(excel_file)
    sheet = workbook.getWorksheets().get(0)
    saveOptions = PdfSaveOptions()
    saveOptions.setOnePagePerSheet(True)
    workbook.save(output_pdf, saveOptions)

    jpype.shutdownJVM()

excel_to_pdf('/app/input_excel_file.xlsx', '/app/output_pdf_file.pdf')

def upload_to_s3(file_path, bucket_name, s3_key):
    try:
        s3.upload_file(file_path, bucket_name, s3_key)
        print(f"File uploaded successfully to s3://{bucket_name}/{s3_key}")
    except Exception as e:
        print(f"Failed to upload file to S3: {e}")

if s3:
    upload_to_s3('/app/output_pdf_file.pdf', 'fitpetbucket', 'example0905')

excel_to_pdf('/app/input_excel_file.xlsx', '/app/output_pdf_file.pdf')
