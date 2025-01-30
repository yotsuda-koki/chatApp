package com.example.chatApp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileUploadController {

	private static final String UPLOAD_DIR = "uploads/";

	/**
	 * ファイルをアップロードするメソッド
	 *
	 * @param file
	 * @return
	 */
	@PostMapping("/upload")
	public String handleFileUpload(@RequestParam("file") MultipartFile file) {

		// 許可する拡張子を設定する
		List<String> allowedExtensions = List.of("jpg", "jpeg", "png", "gif", "pdf");

		String originalFilename = file.getOriginalFilename();

		if (file.isEmpty()) {
			return ""; // 空ファイルのエラーは返さない
		}

		String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
		if (!allowedExtensions.contains(fileExtension)) {
			return ""; // 許可されていない拡張子の場合もエラーは返さない
		}

		try {
			// ファイル名の衝突を防ぐためにUUIDを使用
			String uniqueFileName = UUID.randomUUID() + "_" + originalFilename;
			Path path = Paths.get(UPLOAD_DIR + uniqueFileName);
			Files.write(path, file.getBytes());

			// ダウンロードリンクとしてファイル名を返す
			return "link:" + uniqueFileName;
		} catch (IOException e) {
			e.printStackTrace();
			return ""; // ファイルのアップロードに失敗した場合もエラーは返さない
		}
	}
}
