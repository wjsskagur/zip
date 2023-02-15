package zip.download;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DownloadController {
    private final DownloadUtil downloadUtil;

    @GetMapping("/download/zip")
    public void downloadZip(HttpServletResponse response) {
        // 파일 작업처리를 위한 DTO 선언
        DownloadDto downloadDto = new DownloadDto();
        // 압축할 파일 일므 지정
        downloadDto.setZipFileName("imageZip");
        // 파일 경로를 담아둘 List 선언
        List<String> downloadFileList = new ArrayList<>();

        // ===================================== 여기에 파일 경로를 넣어주세요 ==============================================================
        // 작성자는 Mac 기준으로 작성했습니다.
        downloadFileList.add("/Users/jeon/Documents/JavaProject/Blog/zip/src/main/resources/static/images/beach.jpg");
        downloadFileList.add("/Users/jeon/Documents/JavaProject/Blog/zip/src/main/resources/static/images/bird.jpg");
        downloadFileList.add("/Users/jeon/Documents/JavaProject/Blog/zip/src/main/resources/static/images/corgi.jpg");
        // Window 예시
        // downloadFileList.add("C:\\Users\\jeon\\Documents\\JavaProject\\Blog\\zip\\src\\main\\resources\\static\\images\\beach.jpg");
        // downloadFileList.add("C:\\Users\\jeon\\Documents\\JavaProject\\Blog\\zip\\src\\main\\resources\\static\\images\\bird.jpg");
        // downloadFileList.add("C:\\Users\\jeon\\Documents\\JavaProject\\Blog\\zip\\src\\main\\resources\\static\\images\\bird.jpg");
        downloadDto.setSourceFiles(downloadFileList);
        // ============================================================================================================================


        // 데이터를 담은 DTO 를 압축 파일 생성 및 다운로드를 위한 메소드에 전달
        try {
            downloadUtil.downloadZip(downloadDto, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
