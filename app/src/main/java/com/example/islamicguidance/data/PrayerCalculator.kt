package com.example.islamicguidance.data

import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import kotlin.math.*

enum class CalculationMethod(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val fajrAngle: Double,
    val ishaAngle: Double
) {
    KARACHI(
        "karachi",
        "করাচি বিশ্ববিদ্যালয় (ইসলামিক সায়েন্সেস)",
        "Univ. of Islamic Sciences, Karachi (18° / 18°)",
        18.0,
        18.0
    ),
    MWL(
        "mwl",
        "মুসলিম ওয়ার্ল্ড লীগ (MWL)",
        "Muslim World League (18° / 17°)",
        18.0,
        17.0
    ),
    EGYPTIAN(
        "egypt",
        "মিশরীয় সার্ভে অথরিটি",
        "Egyptian General Authority of Survey (19.5° / 17.5°)",
        19.5,
        17.5
    ),
    MAKKAH(
        "makkah",
        "উম্মুল কুরা, মক্কা",
        "Umm al-Qura University, Makkah (18.5° / 90m)",
        18.5,
        18.5
    )
}

enum class AsrJuristicMethod(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val shadowFactor: Double
) {
    HANAFI(
        "hanafi",
        "হানাফী (দ্বিগুণ ছায়া - পশ্চিমবঙ্গ/উপমহাদেশে প্রচলিত)",
        "Hanafi (2x shadow - Standard in Bengal/Kolkata)",
        2.0
    ),
    SHAFI(
        "shafi",
        "শাফেয়ী / মালেকী / হাম্বলী (একগুণ ছায়া)",
        "Shafi'i / Maliki / Hanbali (1x shadow)",
        1.0
    )
}

data class CityLocation(
    val nameBn: String,
    val nameEn: String,
    val latitude: Double,
    val longitude: Double,
    val timezoneOffsetHours: Double = 5.5,
    val district: String = "Kolkata",
    val state: String = "West Bengal",
    val countryBn: String = "ভারত",
    val countryEn: String = "India",
    val timezoneId: String = "Asia/Kolkata"
)

object PrayerCalculator {

    val supportedCities = listOf(
        CityLocation(
            nameBn = "গার্ডেন রিচ, কলকাতা",
            nameEn = "Garden Reach, Kolkata",
            latitude = 22.5350,
            longitude = 88.3050,
            timezoneOffsetHours = 5.5,
            district = "Kolkata",
            state = "West Bengal",
            countryBn = "ভারত",
            countryEn = "India",
            timezoneId = "Asia/Kolkata"
        ),
        CityLocation("ঢাকা", "Dhaka", 23.8103, 90.4125, 6.0, "ঢাকা", "ঢাকা বিভাগ", "বাংলাদেশ", "Bangladesh", "Asia/Dhaka"),
        CityLocation("চট্টগ্রাম", "Chittagong", 22.3569, 91.7832, 6.0, "চট্টগ্রাম", "চট্টগ্রাম বিভাগ", "বাংলাদেশ", "Bangladesh", "Asia/Dhaka"),
        CityLocation("সিলেট", "Sylhet", 24.8949, 91.8687, 6.0, "সিলেট", "সিলেট বিভাগ", "বাংলাদেশ", "Bangladesh", "Asia/Dhaka"),
        CityLocation("রাজশাহী", "Rajshahi", 24.3636, 88.6241, 6.0, "রাজশাহী", "রাজশাহী বিভাগ", "বাংলাদেশ", "Bangladesh", "Asia/Dhaka"),
        CityLocation("খুলনা", "Khulna", 22.8456, 89.5403, 6.0, "খুলনা", "খুলনা বিভাগ", "বাংলাদেশ", "Bangladesh", "Asia/Dhaka"),
        CityLocation("বরিশাল", "Barisal", 22.7010, 90.3535, 6.0, "বরিশাল", "বরিশাল বিভাগ", "বাংলাদেশ", "Bangladesh", "Asia/Dhaka"),
        CityLocation("রংপুর", "Rangpur", 25.7439, 89.2752, 6.0, "রংপুর", "রংপুর বিভাগ", "বাংলাদেশ", "Bangladesh", "Asia/Dhaka"),
        CityLocation("ময়মনসিংহ", "Mymensingh", 24.7471, 90.4203, 6.0, "ময়মনসিংহ", "ময়মনসিংহ বিভাগ", "বাংলাদেশ", "Bangladesh", "Asia/Dhaka"),
        CityLocation("দিল্লি", "New Delhi", 28.6139, 77.2090, 5.5, "New Delhi", "Delhi", "ভারত", "India", "Asia/Kolkata"),
        CityLocation("মুম্বাই", "Mumbai", 19.0760, 72.8777, 5.5, "Mumbai", "Maharashtra", "ভারত", "India", "Asia/Kolkata"),
        CityLocation("মক্কা মুকাররমা", "Makkah", 21.3891, 39.8579, 3.0, "Makkah", "Hejaz", "সৌদি আরব", "Saudi Arabia", "Asia/Riyadh"),
        CityLocation("মদীনা মুনাওয়ারা", "Madinah", 24.5247, 39.5692, 3.0, "Madinah", "Hejaz", "সৌদি আরব", "Saudi Arabia", "Asia/Riyadh"),
        CityLocation("লন্ডন", "London", 51.5074, -0.1278, 0.0, "London", "England", "যুক্তরাজ্য", "United Kingdom", "Europe/London"),
        CityLocation("নিউইয়র্ক", "New York", 40.7128, -74.0060, -5.0, "New York", "NY", "যুক্তরাষ্ট্র", "USA", "America/New_York"),
        CityLocation("দুবাই", "Dubai", 25.2048, 55.2708, 4.0, "Dubai", "Dubai", "সংযুক্ত আরব আমিরাত", "UAE", "Asia/Dubai")
    )

    const val KAABA_LAT = 21.422487
    const val KAABA_LNG = 39.826206

    fun calculateQiblaBearing(lat: Double, lng: Double): Float {
        val phi1 = Math.toRadians(lat)
        val phi2 = Math.toRadians(KAABA_LAT)
        val deltaLambda = Math.toRadians(KAABA_LNG - lng)

        val y = sin(deltaLambda)
        val x = cos(phi1) * tan(phi2) - sin(phi1) * cos(deltaLambda)
        var qibla = Math.toDegrees(atan2(y, x))
        qibla = (qibla + 360) % 360
        return qibla.toFloat()
    }

    fun calculatePrayerSchedule(
        city: CityLocation,
        calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone(city.timezoneId)),
        method: CalculationMethod = CalculationMethod.KARACHI,
        asrMethod: AsrJuristicMethod = AsrJuristicMethod.HANAFI
    ): PrayerSchedule {
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Fractional year in radians
        val gamma = 2 * Math.PI / 365 * (dayOfYear - 1)

        // Equation of time in minutes
        val eqTime = 229.18 * (0.000075 + 0.001868 * cos(gamma) - 0.032077 * sin(gamma) -
                0.014615 * cos(2 * gamma) - 0.040849 * sin(2 * gamma))

        // Solar declination in radians
        val decl = 0.006918 - 0.399912 * cos(gamma) + 0.070257 * sin(gamma) -
                0.006758 * cos(2 * gamma) + 0.000907 * sin(2 * gamma) -
                0.002697 * cos(3 * gamma) + 0.00148 * sin(3 * gamma)

        val latRad = Math.toRadians(city.latitude)
        val timezone = city.timezoneOffsetHours

        // Solar Noon (local time in hours)
        val solarNoon = 12.0 + timezone - (city.longitude / 15.0) - (eqTime / 60.0)

        // Helper to compute hour angle
        fun hourAngle(zenithDeg: Double): Double {
            val zenithRad = Math.toRadians(zenithDeg)
            val cosH = (cos(zenithRad) - sin(latRad) * sin(decl)) / (cos(latRad) * cos(decl))
            val clamped = cosH.coerceIn(-1.0, 1.0)
            return Math.toDegrees(acos(clamped)) / 15.0
        }

        // Fajr based on calculation method angle
        val haFajr = hourAngle(90.0 + method.fajrAngle)
        val fajrTime = solarNoon - haFajr

        // Sunrise (zenith 90.833)
        val haSunrise = hourAngle(90.833)
        val sunriseTime = solarNoon - haSunrise

        // Dhuhr: solar noon + small buffer (1-2 mins)
        val dhuhrTime = solarNoon + 0.03

        // Asr: based on juristic method (Hanafi 2x vs Standard 1x)
        val noonAltitude = 90.0 - Math.toDegrees(abs(latRad - decl))
        val noonShadow = 1.0 / tan(Math.toRadians(noonAltitude))
        val asrAltitude = Math.toDegrees(atan(1.0 / (noonShadow + asrMethod.shadowFactor)))
        val haAsr = hourAngle(90.0 - asrAltitude)
        val asrTime = solarNoon + haAsr

        // Maghrib (sunset)
        val maghribTime = solarNoon + haSunrise

        // Isha based on calculation method angle
        val haIsha = hourAngle(90.0 + method.ishaAngle)
        val ishaTime = if (method == CalculationMethod.MAKKAH) {
            maghribTime + 1.5 // 90 minutes after Maghrib
        } else {
            solarNoon + haIsha
        }

        fun formatTime(hoursFloat: Double): String {
            var h = (hoursFloat + 24.0) % 24.0
            val hour = h.toInt()
            val min = ((h - hour) * 60).roundToInt()
            val finalHour = if (min == 60) (hour + 1) % 24 else hour
            val finalMin = if (min == 60) 0 else min

            val period = if (finalHour >= 12) "PM" else "AM"
            val displayHour = when {
                finalHour == 0 -> 12
                finalHour > 12 -> finalHour - 12
                else -> finalHour
            }
            return String.format(Locale.US, "%02d:%02d %s", displayHour, finalMin, period)
        }

        fun toMinutes(hoursFloat: Double): Int {
            val h = (hoursFloat + 24.0) % 24.0
            val hour = h.toInt()
            val min = ((h - hour) * 60).roundToInt()
            return hour * 60 + min
        }

        // Current time in city timezone
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        val currentMinutes = currentHour * 60 + currentMinute

        val fajrMin = toMinutes(fajrTime)
        val sunriseMin = toMinutes(sunriseTime)
        val dhuhrMin = toMinutes(dhuhrTime)
        val asrMin = toMinutes(asrTime)
        val maghribMin = toMinutes(maghribTime)
        val ishaMin = toMinutes(ishaTime)

        var nextNameBn = "ফজর"
        var nextNameEn = "Fajr"
        var targetMinutes = fajrMin

        when {
            currentMinutes < fajrMin -> {
                nextNameBn = "ফজর"
                nextNameEn = "Fajr"
                targetMinutes = fajrMin
            }
            currentMinutes < dhuhrMin -> {
                nextNameBn = "যোহর"
                nextNameEn = "Dhuhr"
                targetMinutes = dhuhrMin
            }
            currentMinutes < asrMin -> {
                nextNameBn = "আসর"
                nextNameEn = "Asr"
                targetMinutes = asrMin
            }
            currentMinutes < maghribMin -> {
                nextNameBn = "মাগরিব"
                nextNameEn = "Maghrib"
                targetMinutes = maghribMin
            }
            currentMinutes < ishaMin -> {
                nextNameBn = "ইশা"
                nextNameEn = "Isha"
                targetMinutes = ishaMin
            }
            else -> {
                // Next is tomorrow's Fajr
                nextNameBn = "ফজর"
                nextNameEn = "Fajr"
                targetMinutes = fajrMin + 24 * 60
            }
        }

        var diff = targetMinutes - currentMinutes
        if (diff < 0) diff += 24 * 60
        val remHours = diff / 60
        val remMins = diff % 60

        // Approximate Hijri date
        val (hDay, hMonthBn, hMonthEn, hYear) = getApproximateHijriDate(calendar)

        val gregorianBn = "${convertToBengaliNumerals(day)} ${getBengaliMonthName(month)}, ${convertToBengaliNumerals(year)}"
        val gregorianEn = "$day ${getEnglishMonthName(month)}, $year"

        val hijriDateBn = "${convertToBengaliNumerals(hDay)} $hMonthBn, ${convertToBengaliNumerals(hYear)} হিজরি"
        val hijriDateEn = "$hDay $hMonthEn, $hYear AH"

        val qiblaBearing = calculateQiblaBearing(city.latitude, city.longitude)

        return PrayerSchedule(
            city = city.nameBn,
            country = city.countryBn,
            district = city.district,
            state = city.state,
            fajr = formatTime(fajrTime),
            sunrise = formatTime(sunriseTime),
            dhuhr = formatTime(dhuhrTime),
            asr = formatTime(asrTime),
            maghrib = formatTime(maghribTime),
            isha = formatTime(ishaTime),
            nextPrayerNameBn = nextNameBn,
            nextPrayerNameEn = nextNameEn,
            remainingHours = remHours,
            remainingMinutes = remMins,
            hijriDateBn = hijriDateBn,
            hijriDateEn = hijriDateEn,
            gregorianDateBn = gregorianBn,
            gregorianDateEn = gregorianEn,
            qiblaBearing = qiblaBearing,
            calculationMethodNameBn = method.nameBn,
            calculationMethodNameEn = method.nameEn,
            asrMethodNameBn = asrMethod.nameBn,
            asrMethodNameEn = asrMethod.nameEn
        )
    }

    fun calculateMonthlySchedule(
        city: CityLocation,
        baseCalendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone(city.timezoneId)),
        method: CalculationMethod = CalculationMethod.KARACHI,
        asrMethod: AsrJuristicMethod = AsrJuristicMethod.HANAFI
    ): List<PrayerDaySchedule> {
        val cal = baseCalendar.clone() as Calendar
        val currentDay = baseCalendar.get(Calendar.DAY_OF_MONTH)
        val maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH) + 1
        val year = cal.get(Calendar.YEAR)

        val list = mutableListOf<PrayerDaySchedule>()
        for (d in 1..maxDays) {
            val dayCal = Calendar.getInstance(TimeZone.getTimeZone(city.timezoneId)).apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month - 1)
                set(Calendar.DAY_OF_MONTH, d)
                set(Calendar.HOUR_OF_DAY, 12)
                set(Calendar.MINUTE, 0)
            }
            val schedule = calculatePrayerSchedule(city, dayCal, method, asrMethod)
            val dateBn = "${convertToBengaliNumerals(d)} ${getBengaliMonthName(month)}"
            val dateEn = "$d ${getEnglishMonthName(month)}"
            val hijriBn = schedule.hijriDateBn.substringBefore(",")
            list.add(
                PrayerDaySchedule(
                    dayOfMonth = d,
                    dateFormattedBn = dateBn,
                    dateFormattedEn = dateEn,
                    hijriFormattedBn = hijriBn,
                    fajr = schedule.fajr,
                    sunrise = schedule.sunrise,
                    dhuhr = schedule.dhuhr,
                    asr = schedule.asr,
                    maghrib = schedule.maghrib,
                    isha = schedule.isha,
                    isToday = (d == currentDay)
                )
            )
        }
        return list
    }

    private fun getApproximateHijriDate(calendar: Calendar): Quadruple<Int, String, String, Int> {
        // Epoch calculation for approximate Islamic date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Julian Day Number
        val a = (14 - month) / 12
        val y = year + 4800 - a
        val m = month + 12 * a - 3
        val jdn = day + (153 * m + 2) / 5 + 365 * y + y / 4 - y / 100 + y / 400 - 32045

        // Islamic lunar epoch ~ July 16, 622 CE (JDN 1948440)
        val l = jdn - 1948440 + 10632
        val n = (l - 1) / 10631
        val lPrime = l - 10631 * n + 354
        val j = ((10985 - lPrime) / 5316) * ((50 * lPrime) / 17719) + (lPrime / 5670) * ((43 * lPrime) / 15238)
        val lDoublePrime = lPrime - ((30 - j) / 15) * ((17719 * j) / 50) - (j / 16) * ((15238 * j) / 43) + 29
        val islamicMonth = (24 * lDoublePrime) / 709
        val islamicDay = lDoublePrime - (709 * islamicMonth) / 24
        val islamicYear = 30 * n + j - 30

        val hijriMonthsBn = listOf(
            "মুহাররম", "সফর", "রবিউল আউয়াল", "রবিউস সানী", "জমাদিউল আউয়াল", "জমাদিউস সানী",
            "রজব", "শা'বান", "রমজান", "শাওয়াল", "জিলকদ", "জিলহজ্জ"
        )
        val hijriMonthsEn = listOf(
            "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani", "Jumada al-Awwal", "Jumada al-Thani",
            "Rajab", "Sha'ban", "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
        )

        val clampedMonth = (islamicMonth - 1).coerceIn(0, 11)
        val clampedDay = islamicDay.coerceIn(1, 30)

        return Quadruple(clampedDay, hijriMonthsBn[clampedMonth], hijriMonthsEn[clampedMonth], islamicYear)
    }

    fun convertToBengaliNumerals(number: Int): String {
        val bnDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
        val str = number.toString()
        val sb = StringBuilder()
        for (ch in str) {
            if (ch in '0'..'9') {
                sb.append(bnDigits[ch - '0'])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }

    private fun getBengaliMonthName(month: Int): String {
        val months = listOf("", "জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন", "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর")
        return months.getOrElse(month) { "" }
    }

    private fun getEnglishMonthName(month: Int): String {
        val months = listOf("", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        return months.getOrElse(month) { "" }
    }
}

data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
