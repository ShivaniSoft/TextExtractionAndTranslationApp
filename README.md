# TextExtractionAndTranslationApp
This is smart Android Application for Image-Based Text Extraction and Translation. This project uses Text Recognition API and Text Translation API of Google ML Kit to build this project.
This is multilingual app which provides multiples language for translation. For language identification, I have used Firebase ML Kit API.
Flow of Project
  Step 1: Firstly it will take a input by camera or device storage. The input can be image or document.
  Step 2: After that text recognition api is used to extracting the text from image.
  Step 3: After that language identification api is used to identify the language of text.
  Step 4: And in last text is translated to the target language selected by the user.

Dependencies
Google ML Kit APIs:
    Text Recognition API: "com.google.android.gms:play-services-mlkit-text-recognition:19.0.1"
    Text Translation API: "com.google.mlkit:translate:17.0.3"

Firebase ML Kit API
    Language Identificaton API: "com.google.firebase:firebase-bom:33.12.0"
                                "com.google.firebase:firebase-analytics"
                                "com.google.mlkit:language-id:17.0.6"

  
