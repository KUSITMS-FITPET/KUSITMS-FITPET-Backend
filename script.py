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
    jpype.startJVM()
    workbook = Workbook(excel_file)
    saveOptions = PdfSaveOptions()
    saveOptions.setOnePagePerSheet(True)
    workbook.save(output_pdf, saveOptions)
    jpype.shutdownJVM()

def upload_to_s3(file_path, bucket_name, s3_key):
    try:
        s3_key_with_folder = f"estimatespdf/{s3_key}"

        s3.upload_file(file_path, bucket_name, s3_key_with_folder)

        s3_url = f"https://{bucket_name}.s3.amazonaws.com/{s3_key_with_folder}"
        print(f"s3_url:{s3_url}")
        return s3_url
    except Exception as e:
        print(f"Failed to upload file to S3: {e}")
        return None

if __name__ == "__main__":
    import sys
    excel_file = sys.argv[1]
    pdf_file = sys.argv[2]


    excel_to_pdf(excel_file, pdf_file)

    if s3:
        s3_key = pdf_file.split('/')[-1]
        s3_url = upload_to_s3(pdf_file, 'fitpetbucket', s3_key)
        if s3_url:
            print(f"s3_url:{s3_url}")