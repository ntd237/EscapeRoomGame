import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Question {
    private String question;
    private String[] options;
    private int correctAnswerIndex;
    private static final Random rand = new Random();
    private static final String[][] QUESTIONS;

    // Đọc câu hỏi từ file CSV
    static {
        List<String[]> questionList = new ArrayList<>();
        try {
            // Đọc file từ resources
            InputStream is = Question.class.getClassLoader().getResourceAsStream("questions/questions.csv");
            if (is == null) {
                throw new RuntimeException("Cannot find questions.csv in resources");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                // Parse CSV, xử lý dấu ngoặc kép
                String[] parts = parseCsvLine(line);
                if (parts.length == 6) {
                    questionList.add(parts);
                } else {
                    System.err.println("Invalid CSV line: " + line);
                }
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("Error reading questions.csv: " + e.getMessage());
            // Fallback: Câu hỏi mặc định nếu lỗi
            questionList.add(new String[]{"2 + 2 = ?", "3", "4", "5", "6", "1"});
        }
        QUESTIONS = questionList.toArray(new String[0][]);
    }

    // Parse dòng CSV, xử lý dấu ngoặc kép
    private static String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder field = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(field.toString().trim());
                field = new StringBuilder();
            } else {
                field.append(c);
            }
        }
        result.add(field.toString().trim());
        return result.toArray(new String[0]);
    }

    public Question(String question, String[] options, int correctAnswerIndex) {
        this.question = question;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public static Question getRandomQuestion() {
        int index = rand.nextInt(QUESTIONS.length);
        String[] questionData = QUESTIONS[index];
        return new Question(questionData[0], new String[]{questionData[1], questionData[2], questionData[3], questionData[4]}, Integer.parseInt(questionData[5]));
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}