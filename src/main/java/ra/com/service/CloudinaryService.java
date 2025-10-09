package ra.com.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    public String uplpad(MultipartFile file) {
        try {
            // Lấy tên file gốc từ MultipartFile (bỏ đuôi file)
            String originalFilename = file.getOriginalFilename();
            if (originalFilename != null && originalFilename.contains(".")) {
                originalFilename = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            }

            // Lấy timestamp (epoch time tính bằng giây, UTC)
            long timestamp = System.currentTimeMillis() / 1000L;

            // Thêm tham số upload
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "public_id", originalFilename,   // đặt tên file
                    "timestamp", timestamp           // bắt buộc có timestamp
            );

            // Tạo chữ ký (signature) dựa trên tham số và api_secret
            String signature = cloudinary.apiSignRequest(uploadParams, cloudinary.config.apiSecret);

            // Thêm chữ ký và api_key vào tham số upload
            uploadParams.put("signature", signature);
            uploadParams.put("api_key", cloudinary.config.apiKey);

            // Upload file lên Cloudinary
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);

            return uploadResult.get("secure_url").toString(); // Trả về URL ảnh

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    }
