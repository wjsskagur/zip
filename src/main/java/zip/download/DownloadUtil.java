package zip.download;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
@RequiredArgsConstructor
public class DownloadUtil {

    public void downloadZip(DownloadDto downloadDto, HttpServletResponse response) {

        // 압축될 파일명이 존재하지 않을 경우
        if(downloadDto.getZipFileName() == null || "".equals(downloadDto.getZipFileName()))
            throw new IllegalArgumentException("파일명이 존재하지 않습니다.");

        // 파일이 존재하지 않을 경우
        if(downloadDto.getSourceFiles() == null || downloadDto.getSourceFiles().size() == 0)
            throw new IllegalArgumentException("파일이 존재하지 않습니다.");

        // ======================== 파일 다운로드 위한 response 세팅 ========================
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String(downloadDto.getZipFileName().getBytes(StandardCharsets.UTF_8)) + ".zip;");
        response.setStatus(HttpServletResponse.SC_OK);
        // =============================================================================

        // 본격적인 zip 파일을 만들어 내기 위한 로직
        try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())){

            // List<String> 변수에 담아두었던 파일명을 검색한다
            for (String sourceFile : downloadDto.getSourceFiles()) {
                Path path = Path.of(sourceFile);
                try (FileInputStream fis = new FileInputStream(path.toFile())) {
                    // 압축될 파일명을 ZipEntry에 담아준다
                    ZipEntry zipEntry = new ZipEntry(path.getFileName().toString());
                    // 압축될 파일명을 ZipOutputStream 에 담아준다
                    zos.putNextEntry(zipEntry);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) >= 0) {
                        zos.write(buffer, 0, length);
                    }
                } catch (FileNotFoundException e) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    throw new IllegalArgumentException("파일 변환 작업중, [ " + sourceFile + " ] 파일을 찾을 수 없습니다.");
                } catch (IOException e) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    throw new IllegalArgumentException("파일 변환 작업중, [ " + sourceFile + " ] 파일을 다운로드 할 수 없습니다.");
                } finally {
                    // ZipOutputStream 에 담아둔 압축될 파일명을 flush 시켜준다
                    zos.flush();
                    // ZipOutputStream 에 담아둔 압축될 파일명을 close 시켜준다
                    zos.closeEntry();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // response 에 담아둔 파일을 flush 시켜준다
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
