package zip.download;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DownloadDto {
    private String zipFileName; // 압축될 파일 이름 (xxxx.zip)
    private List<String> sourceFiles; // 압축될 파일 리스트
}