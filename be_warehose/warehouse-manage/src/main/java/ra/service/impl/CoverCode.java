package ra.service.impl;

import org.springframework.stereotype.Component;
import java.util.regex.Pattern;
import java.text.Normalizer;

@Component
public class CoverCode {
    public String getCoverCode(String name) {
        String[] words = name.split(" ");
        StringBuilder abbreviation = new StringBuilder();

        for (String word : words) {
            abbreviation.append(word.charAt(0));
        }

        return abbreviation.toString();
    }

    public String removeVietnameseDiacriticsAndSpaces(String input) {
        // Biểu thức chính quy để tìm các ký tự tiếng Việt có dấu
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        // Thay thế tất cả các ký tự tiếng Việt có dấu bằng khoảng trắng
        String unaccentedString = pattern.matcher(Normalizer.normalize(input, Normalizer.Form.NFD)).replaceAll("").replaceAll(" ", "");

        // Thay thế tất cả các chữ "đ" (viết hoa và viết thường) bằng một ký tự khác hoặc khoảng trắng
        String withoutD = unaccentedString.replaceAll("[đĐ]", "D");

        // Lấy 7 ký tự nhỏ hơn 7 giữ nguyên
        String firstSixCharacters = withoutD.length() > 7 ? withoutD.substring(0, 7) : withoutD;

        return firstSixCharacters;

}}