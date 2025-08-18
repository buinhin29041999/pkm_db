import os
from PIL import Image

def compress_png(input_dir, output_dir, optimize=True, reduce_colors=True):
    # Tạo thư mục output nếu chưa có
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)

    for filename in os.listdir(input_dir):
        if filename.lower().endswith(".png"):
            input_path = os.path.join(input_dir, filename)
            output_path = os.path.join(output_dir, filename)

            try:
                with Image.open(input_path) as img:
                    # Nếu muốn giảm số màu (từ 24-bit xuống 256 màu)
                    if reduce_colors:
                        img = img.convert("P", palette=Image.ADAPTIVE)

                    # Lưu lại với optimize=True để giảm dung lượng
                    img.save(output_path, "PNG", optimize=optimize)
                    print(f"✔ Đã nén {filename} → {output_path}")
            except Exception as e:
                print(f"❌ Lỗi khi xử lý {filename}: {e}")

    print("🎉 Hoàn tất nén PNG!")

if __name__ == "__main__":
    input_dir = r".\image\pokemon_icon"   # thư mục chứa PNG gốc
    output_dir = r".\image\pokemon_icon_r"  # thư mục output

    compress_png(input_dir, output_dir, optimize=True, reduce_colors=True)
