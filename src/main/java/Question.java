import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Question {
    private String question;
    private String[] options;
    private int correctAnswerIndex;

    public Question(String question, String[] options, int correctAnswerIndex) {
        this.question = question;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
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

    public static List<Question> getQuestionBank() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("2 + 2 = ?", new String[]{"3", "4", "5", "6"}, 1));
        questions.add(new Question("Thủ đô của Việt Nam là?", new String[]{"Hà Nội", "TP.HCM", "Đà Nẵng", "Huế"}, 0));
        questions.add(new Question("1kg bằng bao nhiêu gam?", new String[]{"100", "1000", "10000", "10"}, 1));
        questions.add(new Question("Mặt trời mọc ở hướng nào?", new String[]{"Tây", "Đông", "Nam", "Bắc"}, 1));
        questions.add(new Question("Nước có công thức hóa học là gì?", new String[]{"CO2", "H2O", "O2", "N2"}, 1));
        questions.add(new Question("5 x 6 = ?", new String[]{"30", "25", "35", "40"}, 0));
        questions.add(new Question("Hành tinh nào gần Mặt Trời nhất?", new String[]{"Trái Đất", "Sao Hỏa", "Sao Thủy", "Sao Kim"}, 2));
        questions.add(new Question("1 năm có bao nhiêu ngày?", new String[]{"365", "360", "366", "355"}, 0));
        questions.add(new Question("Mèo thuộc họ gì?", new String[]{"Canidae", "Felidae", "Bovidae", "Equidae"}, 1));
        questions.add(new Question("1 + 1 x 0 = ?", new String[]{"0", "1", "2", "11"}, 1));
        return questions;
    }

    public static Question getRandomQuestion() {
        List<Question> questions = getQuestionBank();
        Collections.shuffle(questions);
        return questions.get(0);
    }
}