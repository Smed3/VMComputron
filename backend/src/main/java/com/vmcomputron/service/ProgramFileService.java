package com.vmcomputron.service;

import com.vmcomputron.cvmPackage.CvmRegisters;
import com.vmcomputron.model.ProgramParseResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class ProgramFileService {

    public ProgramParseResponse saveAndParseViaPython(MultipartFile file) {
        try {
            String text = new String(file.getBytes(), StandardCharsets.UTF_8);

            // 1) парсим текст в слова (opcode/operand)
            List<Integer> words = ComputronAsmParser.parse(text);

            // 2) загружаем в VM память
            loadToVm(words);

            // 3) отдаём фронту/ curl результат
            return new ProgramParseResponse(
                    true,
                    null,
                    List.of(),
                    Map.of(
                            "filename", file.getOriginalFilename(),
                            "size", file.getSize(),
                            "wordsCount", words.size(),
                            "words", words,
                            "preview", text.length() > 200 ? text.substring(0, 200) : text
                    )
            );
        } catch (Exception e) {
            return new ProgramParseResponse(false, null, List.of(e.getMessage()), null);
        }
    }

    public ProgramParseResponse saveTextAndParse(String filename, String code) {
        try {
            if (code == null) throw new IllegalArgumentException("code is null");
            String text = stripBom(code);

            // 1) сохранить на диск (внутри проекта, чтобы точно было доступно)
            String safeName = (filename == null || filename.isBlank())
                    ? "program_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt"
                    : filename.replaceAll("[^a-zA-Z0-9._-]", "_");

            Path dir = Path.of("programs");
            Files.createDirectories(dir);
            Path saved = dir.resolve(safeName);
            Files.writeString(saved, text, StandardCharsets.UTF_8);

            // 2) распарсить и загрузить в VM
            var words = ComputronAsmParser.parse(text);
            loadToVm(words);

            return new ProgramParseResponse(
                    true,
                    saved.toAbsolutePath().toString(),
                    List.of(),
                    Map.of(
                            "filename", safeName,
                            "preview", text.length() > 200 ? text.substring(0, 200) : text,
                            "wordsCount", words.size(),
                            "words", words
                    )
            );
        } catch (Exception e) {
            return new ProgramParseResponse(false, null, List.of(e.getMessage()), null);
        }
    }

    private String stripBom(String text) {
        if (!text.isEmpty() && text.charAt(0) == '\uFEFF') return text.substring(1);
        return text;
    }

    private void loadToVm(List<Integer> words) {
        // твой метод как есть
        for (int i = 0; i < 256; i++) com.vmcomputron.cvmPackage.CvmRegisters.setM(i, 0);
        for (int i = 0; i < words.size(); i++) com.vmcomputron.cvmPackage.CvmRegisters.setM(i, words.get(i));
        com.vmcomputron.cvmPackage.CvmRegisters.setPC(0);
    }
}
