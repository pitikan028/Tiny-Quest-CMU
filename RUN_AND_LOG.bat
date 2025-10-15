@echo off
echo Running TinyQuestCMU with detailed logging...
gradle desktop:run --stacktrace --warning-mode all
if exist TinyQuestCMU_error.txt (
  echo.
  echo ===== Detected error log: TinyQuestCMU_error.txt =====
  type TinyQuestCMU_error.txt
) else (
  echo.
  echo (No TinyQuestCMU_error.txt found. If the game window closed unexpectedly, please check console output above.)
)
pause
