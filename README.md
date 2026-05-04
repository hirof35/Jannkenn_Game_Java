# Ultimate Janken Game (Java Swing Edition)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Swing-blue?style=for-the-badge)

Java Swingを使用して開発された、シンプルかつ中毒性の高い本格ジャンケンゲームです。
アニメーション、サウンドエフェクト、および勝利条件システムを搭載しており、CPUとの白熱した心理戦（？）を楽しめます。

## 🚀 主な機能

*   **リアルタイム・シャッフルアニメーション**: CPUの手が決まるまでの緊張感を再現。
*   **インタラクティブGUI**: 画像ベースの直感的なインターフェース。
*   **サウンドシステム**: クリック音、勝利音、敗北音を搭載（再生中の自動停止機能付き）。
*   **自動ジャッジ機能**: 先に5勝したほうが優勝となるトーナメント方式を採用。
*   **レスポンシブな背景変化**: 勝敗結果に応じて画面色がダイナミックに変化。

## 🛠 改良ポイント（最新アップデート）

1.  **オーディオ管理の最適化**:
    *   `Clip`のリソース管理を強化し、新しい音が鳴る際やゲーム終了時に適切に停止・解放するように修正。
2.  **UI/UXのブラッシュアップ**:
    *   アイコンサイズを 80x80 に最適化し、視認性を維持しつつコンパクトなレイアウトに変更。
    *   ゲーム終了時の状態リセット機能を追加。

## 📋 セットアップと実行

### 前提条件
*   JDK 8 以上
*   以下の音声ファイルがルートディレクトリに配置されていること:
    *   `click.wav`
    *   `win.wav`
    *   `lose.wav`
*   以下の画像ファイル（80x80推奨）がルートディレクトリに配置されていること:
    *   `グー.png`
    *   `チョキ.png`
    *   `パー.png`

### 実行方法
```bash
# コンパイル
javac ultimateJankenGame/UltimateJankenGame.java

# 実行
java ultimateJankenGame.UltimateJankenGame
🏗 クラス構成
UltimateJankenGame: メインフレーム。GUIの構築、イベントリスナ、ゲームロジック、タイマー処理をすべてカプセル化。

javax.swing.Timer: CPUの手をシャッフルするアニメーション処理に使用。

javax.sound.sampled.Clip: 低遅延なオーディオ再生を実現。

📝 ライセンス
This project is open-sourced under the MIT License.



<img width="540" height="549" alt="スクリーンショット 2026-05-04 215007" src="https://github.com/user-attachments/assets/af696c0d-1664-480f-ba04-224e6846e45a" />
