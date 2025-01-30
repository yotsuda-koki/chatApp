package com.example.chatApp;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FileDownloadController {

	private static final String UPLOAD_DIR = "uploads/";

	/**
	 * ファイルをダウンロードするメソッド
	 * 
	 * @param filename
	 * @return
	 */
	@GetMapping("/download/{filename:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
		try {
			// ファイルのパスを取得
			Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
			Resource resource = new UrlResource(filePath.toUri());

			// ファイルが存在しない場合
			if (!resource.exists()) {
				return ResponseEntity.notFound().build();
			}

			// ヘッダーにファイルのダウンロード情報を設定
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
}
