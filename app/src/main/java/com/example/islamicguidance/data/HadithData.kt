package com.example.islamicguidance.data

object HadithData {
    val hadiths: List<HadithItem> = listOf(
        // --- 1. SAHIH AL-BUKHARI ---
        HadithItem(
            id = "bukhari_1",
            collection = "সহীহ বুখারী",
            collectionEn = "Sahih al-Bukhari",
            number = 1,
            chapter = "Book of Revelation",
            chapterBn = "ওহীর সূচনা অধ্যায়",
            arabic = "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى",
            bengali = "সকল কাজের ফলাফল নিয়্যতের ওপর নির্ভরশীল। প্রত্যেক ব্যক্তি তাই পাবে যার সে নিয়্যত করেছে।",
            english = "Actions are according to intentions, and everyone will get what was intended.",
            grade = "সহীহ (Sahih - সর্বসম্মতভাবে বিশুদ্ধ)",
            narrator = "উমর ইবনুল খাত্তাব (রা.)",
            category = HadithCategory.FAITH
        ),
        HadithItem(
            id = "bukhari_8",
            collection = "সহীহ বুখারী",
            collectionEn = "Sahih al-Bukhari",
            number = 8,
            chapter = "Book of Faith",
            chapterBn = "ঈমান অধ্যায়",
            arabic = "بُنِيَ الإِسْلاَمُ عَلَى خَمْسٍ شَهَادَةِ أَنْ لاَ إِلَهَ إِلاَّ اللَّهُ وَأَنَّ مُحَمَّدًا رَسُولُ اللَّهِ، وَإِقَامِ الصَّلاَةِ، وَإِيتَاءِ الزَّكَاةِ، وَالْحَجِّ، وَصَوْمِ رَمَضَانَ",
            bengali = "ইসলামের ভিত্তি পাঁচটি স্তম্ভের ওপর স্থাপিত: এ সাক্ষ্য দেওয়া যে, আল্লাহ ছাড়া কোন সত্য উপাস্য নেই এবং মুহাম্মদ (ﷺ) আল্লাহর রাসুল; নামাজ কায়েম করা; যাকাত আদায় করা; হজ পালন করা এবং রমজানের রোজা রাখা।",
            english = "Islam is built upon five pillars: Testifying that there is no god but Allah and Muhammad is the Messenger of Allah, establishing prayer, paying Zakat, Hajj, and fasting Ramadan.",
            grade = "সহীহ (Sahih)",
            narrator = "আব্দুল্লাহ ইবনে উমর (রা.)",
            category = HadithCategory.FAITH
        ),
        HadithItem(
            id = "bukhari_528",
            collection = "সহীহ বুখারী",
            collectionEn = "Sahih al-Bukhari",
            number = 528,
            chapter = "Times of the Prayers",
            chapterBn = "নামাজের সময় অধ্যায়",
            arabic = "سَأَلْتُ النَّبِيَّ صلى الله عليه وسلم أَىُّ الْعَمَلِ أَحَبُّ إِلَى اللَّهِ قَالَ ‏\"‏ الصَّلاَةُ عَلَى وَقْتِهَا ‏\"‏‏.‏ قَالَ ثُمَّ أَىٌّ قَالَ ‏\"‏ ثُمَّ بِرُّ الْوَالِدَيْنِ ‏\"‏‏.‏ قَالَ ثُمَّ أَىٌّ قَالَ ‏\"‏ الْجِهَادُ فِي سَبِيلِ اللَّهِ ‏\"‏‏",
            bengali = "আমি নবী করীম (ﷺ)-কে জিজ্ঞাসা করলাম, আল্লাহর কাছে সর্বাধিক প্রিয় আমল কোনটি? তিনি বললেন: যথাসময়ে নামাজ আদায় করা। আমি বললাম, তারপর কোনটি? তিনি বললেন: পিতা-মাতার সাথে সদ্ব্যবহার করা। আমি বললাম, তারপর কোনটি? তিনি বললেন: আল্লাহর পথে জিহাদ করা।",
            english = "I asked the Prophet (ﷺ): 'Which deed is dearest to Allah?' He replied: 'To offer prayers at their early fixed times.' I asked, 'What is next?' He said: 'To be good and dutiful to parents.'",
            grade = "সহীহ (Sahih)",
            narrator = "আব্দুল্লাহ ইবনে মাসউদ (রা.)",
            category = HadithCategory.PRAYER
        ),
        HadithItem(
            id = "bukhari_6011",
            collection = "সহীহ বুখারী",
            collectionEn = "Sahih al-Bukhari",
            number = 6011,
            chapter = "Book of Good Manners",
            chapterBn = "উত্তম শিষ্টাচার অধ্যায়",
            arabic = "إِنَّ مِنْ خِيَارِكُمْ أَحْسَنَكُمْ أَخْلاَقًا",
            bengali = "নিশ্চয়ই তোমাদের মধ্যে সর্বোত্তম ব্যক্তি সে, যার চরিত্র ও আচরণ সবচেয়ে সুন্দর ও উত্তম।",
            english = "The best amongst you are those who have the best manners and character.",
            grade = "সহীহ (Sahih)",
            narrator = "আব্দুল্লাহ ইবনে আমর (রা.)",
            category = HadithCategory.CHARACTER
        ),
        HadithItem(
            id = "bukhari_5971",
            collection = "সহীহ বুখারী",
            collectionEn = "Sahih al-Bukhari",
            number = 5971,
            chapter = "Book of Good Manners",
            chapterBn = "পিতা-মাতার সম্মান অধ্যায়",
            arabic = "جَاءَ رَجُلٌ إِلَى رَسُولِ اللَّهِ صلى الله عليه وسلم فَقَالَ مَنْ أَحَقُّ النَّاسِ بِحُسْنِ صَحَابَتِي قَالَ ‏\"‏ أُمُّكَ ‏\"‏‏.‏ قَالَ ثُمَّ مَنْ قَالَ ‏\"‏ ثُمَّ أُمُّكَ ‏\"‏‏.‏ قَالَ ثُمَّ مَنْ قَالَ ‏\"‏ ثُمَّ أُمُّكَ ‏\"‏‏.‏ قَالَ ثُمَّ مَنْ قَالَ ‏\"‏ ثُمَّ أَبُوكَ ‏\"‏‏",
            bengali = "এক ব্যক্তি রাসূলুল্লাহ (ﷺ)-এর কাছে এসে জিজ্ঞাসা করল: মানুষের মধ্যে আমার সবচেয়ে উত্তম আচরণের বেশি হকদার কে? তিনি বললেন: তোমার মা। লোকটি বলল: তারপর কে? তিনি বললেন: তোমার মা। লোকটি বলল: তারপর কে? তিনি বললেন: তোমার মা। লোকটি বলল: তারপর কে? তিনি বললেন: তোমার পিতা।",
            english = "A man asked the Prophet (ﷺ): 'Who is most worthy of my good companionship?' He said: 'Your mother.' The man asked, 'Then who?' He said: 'Your mother.' The man asked, 'Then who?' He said: 'Your mother.' The man asked, 'Then who?' He said: 'Then your father.'",
            grade = "সহীহ (Sahih)",
            narrator = "আবু হুরায়রা (রা.)",
            category = HadithCategory.FAMILY
        ),

        // --- 2. SAHIH MUSLIM ---
        HadithItem(
            id = "muslim_223",
            collection = "সহীহ মুসলিম",
            collectionEn = "Sahih Muslim",
            number = 223,
            chapter = "Book of Purification",
            chapterBn = "পবিত্রতা অধ্যায়",
            arabic = "الطُّهُورُ شَطْرُ الإِيمَانِ وَالْحَمْدُ لِلَّهِ تَمْلأُ الْمِيزَانَ وَسُبْحَانَ اللَّهِ وَالْحَمْدُ لِلَّهِ تَمْلآنِ - أَوْ تَمْلأُ - مَا بَيْنَ السَّمَاوَاتِ وَالأَرْضِ",
            bengali = "পবিত্রতা ঈমানের অর্ধেক। 'আলহামদুলিল্লাহ' নেক আমলের পাল্লাকে পূর্ণ করে দেয়। আর 'সুবহানাল্লাহ' ও 'আলহামদুলিল্লাহ' আকাশ ও পৃথিবীর মধ্যবর্তী স্থানকে সওয়াব দিয়ে পরিপূর্ণ করে দেয়।",
            english = "Purity is half of faith, and 'Alhamdulillah' fills the scale, and 'SubhanAllah' and 'Alhamdulillah' fill what is between heaven and earth.",
            grade = "সহীহ (Sahih)",
            narrator = "আবু মালেক আল-আশআরী (রা.)",
            category = HadithCategory.PRAYER
        ),
        HadithItem(
            id = "muslim_2699",
            collection = "সহীহ মুসলিম",
            collectionEn = "Sahih Muslim",
            number = 2699,
            chapter = "Book of Remembrance and Knowledge",
            chapterBn = "ইলম ও দ্বীন শিক্ষা অধ্যায়",
            arabic = "مَنْ سَلَكَ طَرِيقًا يَلْتَمِسُ فِيهِ عِلْمًا سَهَّلَ اللَّهُ لَهُ بِهِ طَرِيقًا إِلَى الْجَنَّةِ",
            bengali = "যে ব্যক্তি ইলম (দ্বীনি জ্ঞান) অর্জনের জন্য কোনো পথ অবলম্বন করে, আল্লাহ তার জন্য জান্নাতের পথ সহজ করে দেন।",
            english = "Whoever treads a path in search of knowledge, Allah will make easy for him the path to Paradise.",
            grade = "সহীহ (Sahih)",
            narrator = "আবু হুরায়রা (রা.)",
            category = HadithCategory.KNOWLEDGE
        ),
        HadithItem(
            id = "muslim_2702",
            collection = "সহীহ মুসলিম",
            collectionEn = "Sahih Muslim",
            number = 2702,
            chapter = "Book of Repentance",
            chapterBn = "তওবা ও ক্ষমা অধ্যায়",
            arabic = "إِنَّ اللَّهَ يَبْسُطُ يَدَهُ بِاللَّيْلِ لِيَتُوبَ مُسِيءُ النَّهَارِ وَيَبْسُطُ يَدَهُ بِالنَّهَارِ لِيَتُوبَ مُسِيءُ اللَّيْلِ حَتَّى تَطْلُعَ الشَّمْسُ مِنْ مَغْرِبِهَا",
            bengali = "নিশ্চয়ই আল্লাহ তাআলা রাতে তাঁর হাত প্রসারিত করেন যাতে দিনে গুনাহকারী ব্যক্তি তওবা করে, এবং দিনে হাত প্রসারিত করেন যাতে রাতের গুনাহকারী তওবা করে—যতক্ষণ না পশ্চিম দিক হতে সূর্য উদিত হয়।",
            english = "Allah stretches out His hand at night so that those who sinned by day may repent, and by day so that those who sinned at night may repent, until the sun rises from the west.",
            grade = "সহীহ (Sahih)",
            narrator = "আবু মূসা আল-আশআরী (রা.)",
            category = HadithCategory.FAITH
        ),

        // --- 3. SUNAN ABU DAWUD ---
        HadithItem(
            id = "abudawud_1479",
            collection = "সুনান আবু দাউদ",
            collectionEn = "Sunan Abi Dawud",
            number = 1479,
            chapter = "Book of Prayer (Du'a)",
            chapterBn = "দোয়া ও মুনাজাত অধ্যায়",
            arabic = "الدُّعَاءُ هُوَ الْعِبَادَةُ ، ثُمَّ قَرَأَ: {وَقَالَ رَبُّكُمُ ادْعُونِي أَسْتَجِبْ لَكُمْ}",
            bengali = "দোয়াই হলো মূল ইবাদত। অতঃপর রাসূলুল্লাহ (ﷺ) পাঠ করলেন: 'তোমাদের রব বলেছেন: তোমরা আমাকে ডাকো, আমি তোমাদের ডাকে সাড়া দেব।' (সূরা গাফির: ৬০)",
            english = "Supplication (du'a) is worship itself. Then the Prophet (ﷺ) recited: 'And your Lord says: Call upon Me; I will respond to you.'",
            grade = "সহীহ (Sahih - আলবানী সহীহ বলেছেন)",
            narrator = "নুমান ইবনে বাশীর (রা.)",
            category = HadithCategory.DAILY
        ),
        HadithItem(
            id = "abudawud_4607",
            collection = "সুনান আবু দাউদ",
            collectionEn = "Sunan Abi Dawud",
            number = 4607,
            chapter = "Book of Sunnah",
            chapterBn = "সুন্নাহর অনুসরণ অধ্যায়",
            arabic = "فَعَلَيْكُمْ بِسُنَّتِي وَسُنَّةِ الْخُلَفَاءِ الْمَهْدِيِّينَ الرَّاشِدِينَ تَمَسَّكُوا بِهَا وَعَضُّوا عَلَيْهَا بِالنَّوَاجِذِ",
            bengali = "তোমরা অবশ্যই আমার সুন্নাহ এবং হিদায়াতপ্রাপ্ত খোলাফায়ে রাশেদীনের সুন্নাহ দৃঢ়ভাবে ধারণ করবে এবং তা দাঁত দিয়ে কামড়ে আঁকড়ে ধরে থাকবে।",
            english = "You must adhere strictly to my Sunnah and the way of the rightly-guided caliphs; hold fast to it and cling to it stubbornly.",
            grade = "সহীহ (Sahih)",
            narrator = "ইরবায ইবনে সারিয়াহ (রা.)",
            category = HadithCategory.KNOWLEDGE
        ),
        HadithItem(
            id = "abudawud_4941",
            collection = "সুনান আবু দাউদ",
            collectionEn = "Sunan Abi Dawud",
            number = 4941,
            chapter = "Book of Manners",
            chapterBn = "প্রতিবেশীর অধিকার অধ্যায়",
            arabic = "لاَ يَدْخُلُ الْجَنَّةَ مَنْ لاَ يَأْمَنُ جَارُهُ بَوَائِقَهُ",
            bengali = "সেই ব্যক্তি জান্নাতে প্রবেশ করবে না, যার প্রতিবেশী তার অনিষ্ট ও ক্ষতিসাধন থেকে নিরাপদ থাকে না।",
            english = "He will not enter Paradise whose neighbor does not feel safe from his harm.",
            grade = "সহীহ (Sahih)",
            narrator = "আবু হুরায়রা (রা.)",
            category = HadithCategory.CHARACTER
        ),

        // --- 4. JAMI' AT-TIRMIDHI ---
        HadithItem(
            id = "tirmidhi_2516",
            collection = "জামে আত-তিরমিযী",
            collectionEn = "Jami` at-Tirmidhi",
            number = 2516,
            chapter = "Description of the Day of Judgment",
            chapterBn = "আল্লাহর ওপর নির্ভরতা ও তাওয়াক্কুল",
            arabic = "احْفَظِ اللَّهَ يَحْفَظْكَ، احْفَظِ اللَّهَ تَجِدْهُ تُجَاهَكَ، إِذَا سَأَلْتَ فَاسْأَلِ اللَّهَ، وَإِذَا اسْتَعَنْتَ فَاسْتَعِنْ بِاللَّهِ",
            bengali = "তুমি আল্লাহর হুকুম হেফাজত কর, আল্লাহ তোমাকে হেফাজত করবেন। তুমি আল্লাহকে স্মরণে রাখ, তুমি তাঁকে তোমার সামনে পাবে। যখন কিছু চাইবে কেবল আল্লাহর কাছেই চাইবে; আর যখন সাহায্য প্রার্থনা করবে কেবল আল্লাহর কাছেই সাহায্য চাইবে।",
            english = "Be mindful of Allah and He will protect you. Be mindful of Allah and you will find Him before you. If you ask, ask of Allah; and if you seek help, seek help from Allah.",
            grade = "হাসান সহীহ (Hasan Sahih)",
            narrator = "আব্দুল্লাহ ইবনে আব্বাস (রা.)",
            category = HadithCategory.FAITH
        ),
        HadithItem(
            id = "tirmidhi_1956",
            collection = "জামে আত-তিরমিযী",
            collectionEn = "Jami` at-Tirmidhi",
            number = 1956,
            chapter = "Book of Righteousness",
            chapterBn = "সদাচরণ ও দান-সদকা অধ্যায়",
            arabic = "تَبَسُّمُكَ فِي وَجْهِ أَخِيكَ لَكَ صَدَقَةٌ",
            bengali = "তোমার মুসলিম ভাইয়ের মুখের দিকে তাকিয়ে তোমার মুচকি হাসি দেওয়াও তোমার জন্য একটি সদকা।",
            english = "Your smiling in the face of your brother is charity for you.",
            grade = "হাসান (Hasan - তিরমিযী কর্তৃক উত্তম সনদে বর্ণিত)",
            narrator = "আবু যার গিফারী (রা.)",
            category = HadithCategory.CHARITY
        ),
        HadithItem(
            id = "tirmidhi_2307",
            collection = "জামে আত-তিরমিযী",
            collectionEn = "Jami` at-Tirmidhi",
            number = 2307,
            chapter = "Book of Zuhd (Asceticism)",
            chapterBn = "কথা নিয়ন্ত্রণ ও অনর্থক কাজ ত্যাগ",
            arabic = "مِنْ حُسْنِ إِسْلاَمِ الْمَرْءِ تَرْكُهُ مَا لاَ يَعْنِيهِ",
            bengali = "মানুষের ইসলামের অন্যতম সৌন্দর্য হলো তার জন্য অনর্থক ও অপ্রয়োজনীয় বিষয় বর্জন করা।",
            english = "Part of the perfection of a person's Islam is leaving that which does not concern him.",
            grade = "হাসান (Hasan)",
            narrator = "আলী ইবনুল হুসাইন (রহ.)",
            category = HadithCategory.CHARACTER
        ),

        // --- 5. SUNAN AN-NASA'I ---
        HadithItem(
            id = "nasai_1344",
            collection = "সুনান আন-নাসায়ী",
            collectionEn = "Sunan an-Nasa'i",
            number = 1344,
            chapter = "Book of Seeking Refuge",
            chapterBn = "জান্নাত প্রার্থনা ও জাহান্নাম থেকে মুক্তি",
            arabic = "مَنْ سَأَلَ اللَّهَ الْجَنَّةَ ثَلاَثَ مَرَّاتٍ قَالَتِ الْجَنَّةُ اللَّهُمَّ أَدْخِلْهُ الْجَنَّةَ وَمَنِ اسْتَجَارَ مِنَ النَّارِ ثَلاَثَ مَرَّاتٍ قَالَتِ النَّارُ اللَّهُمَّ أَجِرْهُ مِنَ النَّارِ",
            bengali = "যে ব্যক্তি তিনবার আল্লাহর কাছে জান্নাত প্রার্থনা করে, জান্নাত বলে: 'হে আল্লাহ! তাকে জান্নাতে প্রবেশ করান।' আর যে ব্যক্তি তিনবার জাহান্নামের আগুন থেকে আশ্রয় চায়, জাহান্নাম বলে: 'হে আল্লাহ! তাকে আগুন থেকে রক্ষা করুন।'",
            english = "Whoever asks Allah for Paradise three times, Paradise says: 'O Allah, admit him to Paradise.' And whoever seeks refuge from the Fire three times, the Fire says: 'O Allah, save him from the Fire.'",
            grade = "সহীহ (Sahih - ইমাম আলবানী বিশুদ্ধ বলেছেন)",
            narrator = "আনাস ইবনে মালিক (রা.)",
            category = HadithCategory.DAILY
        ),
        HadithItem(
            id = "nasai_3104",
            collection = "সুনান আন-নাসায়ী",
            collectionEn = "Sunan an-Nasa'i",
            number = 3104,
            chapter = "Book of Jihad",
            chapterBn = "মায়ের সেবার মর্যাদা অধ্যায়",
            arabic = "الْزَمْ رِجْلَهَا فَثَمَّ الْجَنَّةُ",
            bengali = "রাসূলুল্লাহ (ﷺ) বললেন: তুমি তোমার মায়ের সেবায় নিজেকে নিয়োজিত রাখো, কেননা জান্নাত তাঁর পায়ের কাছে রয়েছে।",
            english = "Stick to her feet, for Paradise is right there.",
            grade = "হাসান সহীহ (Hasan Sahih)",
            narrator = "মুআবিয়া ইবনে জাহিমা (রা.)",
            category = HadithCategory.FAMILY
        ),

        // --- 6. SUNAN IBN MAJAH ---
        HadithItem(
            id = "ibnmajah_224",
            collection = "সুনান ইবনে মাজাহ",
            collectionEn = "Sunan Ibn Majah",
            number = 224,
            chapter = "Book of Sunnah",
            chapterBn = "দ্বীনি ইলম অর্জন অধ্যায়",
            arabic = "طَلَبُ الْعِلْمِ فَرِيضَةٌ عَلَى كُلِّ مُسْلِمٍ",
            bengali = "দ্বীনি জ্ঞান (ইলম) অর্জন করা প্রত্যেক মুসলিম নর-নারীর ওপর আবশ্যকীয় ফরজ।",
            english = "Seeking knowledge is an obligation upon every Muslim.",
            grade = "সহীহ (Sahih - বহু সূত্রে বর্ণিত বিশুদ্ধ হাদিস)",
            narrator = "আনাস ইবনে মালিক (রা.)",
            category = HadithCategory.KNOWLEDGE
        ),
        HadithItem(
            id = "ibnmajah_4174",
            collection = "সুনান ইবনে মাজাহ",
            collectionEn = "Sunan Ibn Majah",
            number = 4174,
            chapter = "Book of Zuhd",
            chapterBn = "দুনিয়ার মোহমুক্তির মর্যাদা",
            arabic = "ازْهَدْ فِي الدُّنْيَا يُحِبَّكَ اللَّهُ، وَازْهَدْ فِيمَا فِي أَيْدِي النَّاسِ يُحِبَّكَ النَّاسُ",
            bengali = "তুমি দুনিয়ার মোহ ত্যাগ করো, আল্লাহ তোমাকে ভালোবাসবেন; আর মানুষের কাছে যা আছে তার প্রতি নির্লোভ থাকো, মানুষ তোমাকে ভালোবাসবে।",
            english = "Be indifferent towards the world, and Allah will love you. Be indifferent towards what people possess, and people will love you.",
            grade = "হাসান (Hasan)",
            narrator = "সাহল ইবনে সাদ আস-সাঈদী (রা.)",
            category = HadithCategory.CHARACTER
        ),
        HadithItem(
            id = "ibnmajah_1388",
            collection = "সুনান ইবনে মাজাহ",
            collectionEn = "Sunan Ibn Majah",
            number = 1388,
            chapter = "Establishing the Prayer",
            chapterBn = "শবে বরাত সংক্রান্ত রেওয়ায়াত পর্যালোচনা",
            arabic = "إِذَا كَانَتْ لَيْلَةُ النِّصْفِ مِنْ شَعْبَانَ فَقُومُوا لَيْلَهَا وَصُومُوا نَهَارَهَا",
            bengali = "যখন শা'বানের মধ্যবর্তী রাত (১৫ই শাবান) আসে, তখন তোমরা রাতে সালাত আদায় কর এবং দিনে রোজা রাখ। [সতর্কবার্তা: মুহাদ্দিসগণের ঐক্যমতে এর সনদে ইবনে আবি সাবরাহ নামক বর্ণনাকারী অত্যন্ত দুর্বল/পরিত্যাজ্য]。",
            english = "When the middle night of Sha'ban comes, spend its night in prayer and fast its day. [Scholarly Note: Weak chain due to Ibn Abi Sabrah].",
            grade = "দুর্বল / যয়ীফ (Da'if - আলবানী ও ইমামগণের মতে সনদ অত্যন্ত দুর্বল)",
            narrator = "আলী ইবনে আবি তালিব (রা.)",
            category = HadithCategory.FASTING
        )
    )

    val dailyHadith = hadiths[0]
}
