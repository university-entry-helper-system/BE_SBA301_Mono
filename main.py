import hmac
import hashlib
from urllib.parse import urlencode

params = {
    "vnp_TmnCode": "DNA3EMRJ",
    "vnp_TxnRef": "1",
    "vnp_Amount": "100000",
    "vnp_BankCode": "NCB",
    "vnp_OrderInfo": "Thanh toan don hang 1",
    "vnp_ResponseCode": "00",
    "vnp_TransactionStatus": "00",
    "vnp_TransactionNo": "12345678"
}

secret_key = "7ZLNDHBI4PMZK566GJTEDT7HQE8J8NKQ"

# Bước 1: Sắp xếp thứ tự alphabet
sorted_params = sorted(params.items())

# Bước 2: Tạo query string đúng định dạng
query_string = urlencode(sorted_params)

# Bước 3: Tạo chữ ký HMAC SHA512
vnp_SecureHash = hmac.new(secret_key.encode('utf-8'), query_string.encode('utf-8'), hashlib.sha512).hexdigest()

print("Chuỗi ký:", query_string)
print("vnp_SecureHash =", vnp_SecureHash)
