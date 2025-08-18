import os
from PIL import Image

def convert_png_to_jpg(input_dir, output_dir, quality=85):
    # Tạo thư mục output nếu chưa có
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)

    # Lặp qua tất cả file PNG
    for filename in os.listdir(input_dir):
        if filename.lower().endswith(".png"):
            png_path = os.path.join(input_dir, filename)
            jpg_filename = os.path.splitext(filename)[0] + ".jpg"
            jpg_path = os.path.join(output_dir, jpg_filename)

            try:
                # Mở ảnh PNG
                with Image.open(png_path) as img:
                    # Chuyển sang RGB (JPG không hỗ trợ alpha)
                    rgb_img = img.convert("RGB")
                    # Lưu JPG với quality (0–100)
                    rgb_img.save(jpg_path, "JPEG", quality=quality)
                    print(f"✔ Đã chuyển {filename} → {jpg_filename}")
            except Exception as e:
                print(f"❌ Lỗi khi xử lý {filename}: {e}")

    print("🎉 Hoàn tất chuyển đổi PNG → JPG!")

if __name__ == "__main__":
    input_dir = "../image/png"   # thư mục chứa PNG
    output_dir = "C:/images/jpg"  # thư mục chứa JPG
    convert_png_to_jpg(input_dir, output_dir, quality=85)
