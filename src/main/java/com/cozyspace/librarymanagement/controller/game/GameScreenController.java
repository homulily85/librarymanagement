package com.cozyspace.librarymanagement.controller.game;

import com.cozyspace.librarymanagement.DataTransfer;
import com.cozyspace.librarymanagement.datasource.Document;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.*;

public class GameScreenController {

    @FXML
    private TextField guessTextField;
    @FXML
    private Button submitButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button skipButton;
    @FXML
    private Button exitButton;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label highscoreLabel;

    private Document correctAnswer;
    private List<Document> remainingDocuments;
    private int score;
    private int incorrectGuessCount;
    private int highscore;
    private Timeline countdownTimer;
    private int timeElapsed = 0;

    private static final String HIGH_SCORE_FILE = "resources/highscore.txt";

    public void initialize() {
        List<Document> documentList = loadDocuments();
        remainingDocuments = new ArrayList<>(documentList);

        score = 0;
        highscore = loadHighscore();
        updateHighscoreLabel();

        incorrectGuessCount = 0;
        updateScoreLabel();
        loadGame();

        guessTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSubmitButton();
            }
        });
    }

    private void updateHighscoreLabel() {
        highscoreLabel.setText("Highscore: " + highscore);
    }

    private void saveHighscore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGH_SCORE_FILE))) {
            writer.write(String.valueOf(highscore));
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu kỉ lục: " + e.getMessage());
        }
    }

    private int loadHighscore() {
        int loadedHighscore = 0;
        File file = new File(HIGH_SCORE_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine();
                if (line != null) {
                    loadedHighscore = Integer.parseInt(line);
                }
            } catch (IOException | NumberFormatException e) {
                System.out.println("Lỗi khi đọc kỉ lục: " + e.getMessage());
            }
        }
        return loadedHighscore;
    }

    private List<Document> loadDocuments() {
        List<Document> documents = new ArrayList<>();
        HashMap<String, String> dataMap = DataTransfer.getInstance().getDataMap();
        String documentsData = dataMap.get("documents");

        if (documentsData != null && !documentsData.isEmpty()) {
            documents = parseStringToDocuments(documentsData);
        }

        if (documents.isEmpty()) {
            System.out.println("Không tìm thấy tài liệu nào, tạo danh sách dự phòng.");
            documents = createFallbackDocuments();
        }

        return documents;
    }

    private List<Document> createFallbackDocuments() {
        List<Document> fallbackDocuments = new ArrayList<>();
        fallbackDocuments.add(new Document("978-604-1-14078-3", "Mắt biếc", "Nguyễn Nhật Ánh", "Cuốn sách nói về tình yêu thầm lặng của một chàng trai dành cho cô gái mà anh yêu suốt thời thơ ấu. Đây là một câu chuyện buồn về tình yêu đơn phương, sự hy sinh và những kỷ niệm không bao giờ phai.", "Tiểu thuyết tình cảm", 5, "Chủ đề 1", "cover1.png"));
        fallbackDocuments.add(new Document("978-604-2-31238-7", "Hoàng tử bé", "Antoine de Saint Exupéry", "Cuốn sách này kể về một cuộc gặp gỡ kỳ lạ giữa một phi công" + " và một hoàng tử nhỏ từ một hành tinh xa lạ", "Tiểu thuyết văn học", 3, "Chủ đề 2", "cover2.png"));
        fallbackDocuments.add(new Document("1-4391-6734-6", "Đắc nhân tâm", "Dale Carnegie", "là cuốn sách đem lại những giá trị tuyệt vời cho người đọc, bao gồm những lời khuyên cực kỳ bổ ích về cách ứng xử trong cuộc sống hàng ngày.", "Sách kỹ năng sống", 7, "Chủ đề 3", "cover3.png"));
        return fallbackDocuments;
    }

    /**
     * Chuyển đổi chuỗi dữ liệu thành danh sách tài liệu.
     *
     * @param data Chuỗi chứa thông tin tài liệu.
     * @return Danh sách các tài liệu.
     */
    private List<Document> parseStringToDocuments(String data) {
        List<Document> documents = new ArrayList<>();
        String[] documentEntries = data.split(";");

        for (int i = 0; i < documentEntries.length; i++) {
            String entry = documentEntries[i].trim();
            if (entry.isEmpty()) {
                System.out.println("Bỏ qua mục rỗng tại vị trí: " + i);
                continue;
            }

            String[] fields = entry.split(",");
            try {
                Document document = new Document(
                        fields[0].trim(),
                        fields[1].trim(),
                        fields[2].trim(),
                        fields[3].trim(),
                        fields[4].trim(),
                        Integer.parseInt(fields[5].trim()),
                        fields[6].trim(),
                        fields[7].trim()
                );
                documents.add(document);
            } catch (Exception e) {
                System.out.println("Lỗi");
            }
        }

        return documents;
    }

    /**
     * Tải câu hỏi mới
     */
    private void loadGame() {
        if (remainingDocuments.isEmpty()) {
            showAlert("Thông báo", "Không còn tài liệu nào để chơi. Bạn đã hoàn thành trò chơi!", Alert.AlertType.INFORMATION);
            disableGame();
            return;
        }

        Random random = new Random();
        int index = random.nextInt(remainingDocuments.size());
        correctAnswer = remainingDocuments.get(index);
        remainingDocuments.remove(index);
        String hint = String.format("Tác giả: %s\nMô tả: %s\nLoại tài liệu: %s", correctAnswer.getAuthor(), correctAnswer.getDescription(), correctAnswer.getType());
        descriptionLabel.setText(hint);
        guessTextField.clear();
        submitButton.setDisable(false);
        nextButton.setDisable(true);
        skipButton.setDisable(false);
        startCountdown();
    }

    private void startCountdown() {
        timeElapsed = 0;
        if (countdownTimer != null) {
            countdownTimer.stop();
        }

        countdownTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeElapsed++;
            updateTimerLabel();
        }));

        countdownTimer.setCycleCount(Timeline.INDEFINITE);
        countdownTimer.play();
    }

    private void updateTimerLabel() {
        timeLabel.setText("Thời gian: " + timeElapsed + " giây ");
    }

    private int calculatePoints(int timeElapsed) {
        if (timeElapsed <= 10) {
            return 3;
        } else if (timeElapsed <= 15) {
            return 2;
        } else {
            return 1;
        }
    }

    /**
     * Xử lý khi nhấn nút "Thoát"
     */
    @FXML
    private void handleExitButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText("Bạn có chắc muốn thoát khỏi game?");
        alert.setContentText("Mọi tiến trình chơi sẽ không được lưu.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cozyspace/librarymanagement/fxml/member/member_main_screen.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) exitButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Xử lý khi nhấn nút "Submit"
     */
    @FXML
    private void handleSubmitButton() {
        String userGuess = guessTextField.getText().trim();

        if (userGuess.equalsIgnoreCase(correctAnswer.getTitle())) {
            int earnedPoints = calculatePoints(timeElapsed);
            score += earnedPoints;
            updateScoreLabel();
            submitButton.setDisable(true);
            incorrectGuessCount = 0;
            showAlert("Chúc mừng!", "Bạn đã đoán đúng!", Alert.AlertType.INFORMATION);
        } else {
            incorrectGuessCount++;
            if (incorrectGuessCount < 3) {
                showAlert("Sai rồi!", "Đáp án sai. Bạn còn " + (3 - incorrectGuessCount) + " lần thử nữa.", Alert.AlertType.ERROR);
            } else {
                showAlert("Sai rồi!", "Đáp án sai. Đáp án đúng là: " + correctAnswer.getTitle(), Alert.AlertType.ERROR);
                submitButton.setDisable(true);
                incorrectGuessCount = 0;
            }
        }
        if (score > highscore) {
            highscore = score;
            updateHighscoreLabel();
            saveHighscore();
        }

        nextButton.setDisable(false);
    }

    @FXML
    private void handleNextButton() {
        loadGame();
    }

    @FXML
    private void handleSkipButton() {
        showAlert("Bỏ qua", "Bạn đã bỏ qua câu hỏi này!", Alert.AlertType.INFORMATION);
        loadGame();
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Điểm: " + score);
    }

    private void disableGame() {
        submitButton.setDisable(true);
        nextButton.setDisable(true);
        guessTextField.setDisable(true);
    }

    /**
     * Hiển thị thông báo
     *
     * @param title     Tiêu đề
     * @param message   Nội dung
     * @param alertType Loại thông báo
     */
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
