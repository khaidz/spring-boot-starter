package net.khaibq.springbootstater.dto.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDto {
    String[] emailTo;
    String[] emailCC;
    String[] emailBCC;
    String subject;
    String template;
    String content;
    Map<String, Object> params;
    File[] files;
    String[] fileNames;

    @Override
    public String toString() {
        return "EmailObjectRequest{" +
               "emailTo=" + Arrays.toString(emailTo) +
               ", emailCC=" + Arrays.toString(emailCC) +
               ", emailBCC=" + Arrays.toString(emailBCC) +
               ", subject='" + subject + '\'' +
               ", template='" + template + '\'' +
               ", content='" + content + '\'' +
               ", params=" + params +
               ", files=" + Arrays.toString(files) +
               ", fileNames=" + Arrays.toString(fileNames) +
               '}';
    }
}