package com.cetdhwani.bottlejump;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;

public class Localstore {

	SharedPreferences.Editor editor;
	SharedPreferences pref;
	Context con;;

	public Localstore(Context a) {
		con = a;

		pref = a.getSharedPreferences("PrefDh", Context.MODE_PRIVATE);
		editor = pref.edit();
		createGameSettings();
	}

	void createGameSettings() {
		if (!pref.contains("sound")) {
			editor.putInt("sound", 1);
			editor.commit();
		}
		if (!pref.contains("vibrate")) {
			editor.putInt("vibrate", 1);
			editor.commit();
		}
	}

	public boolean getSoundVal() {
		if (pref.getInt("sound", 0) == 1)
			return true;
		return false;
	}

	public boolean getVibrationVal() {
		if (pref.getInt("vibrate", 0) == 1)
			return true;
		return false;
	}

	public void toggleSound() {
		editor.putInt("sound", 1 - pref.getInt("sound", 0));
		editor.commit();
	}

	public void toggleVib() {
		editor.putInt("vibrate", 1 - pref.getInt("vibrate", 0));
		editor.commit();
	}

	void putdata(String a, String b) {
		MCrypt e = new MCrypt();
		String js = "";
		try {
			js = e.bytesToHex(e.encrypt(b));

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		editor.putString(a, js).commit();

	}

	public void clrstore() {
		if (pref.getInt("fir", 1) != 0) {
			pref.edit().clear().commit();
			pref.edit().putInt("fir", 0).commit();

		}
	}

	public String getdata(String a) {
		String b = "";
		MCrypt e = new MCrypt();
		b = pref.getString(a, "");
		String decrypted = "";
		try {
			decrypted = new String(e.decrypt(b));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return decrypted;
	}

	JSONObject score() {
		JSONObject b = null;
		try {
			b = new JSONObject(getdata("ghjk"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}

	public int getscore() {
		JSONObject b = null;
		try {
			String de = "";
			b = new JSONObject(getdata("ghjk"));

			return b.getInt("score");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public void changeversion(int v, int m) {

		JSONObject b = null;
		try {
			b = new JSONObject();
			b.put("vz", v);
			b.put("mz", m);
			putdata("vbcx", b.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int getv() {
		new SendData(pref.getString("asdf", ""),
				"http://cetdhwani.com/bottlerun/version.php", 5, con).execute();

		try {
			JSONObject b = new JSONObject(getdata("vbcx"));
			int v = b.getInt("vz");
			int m = b.getInt("mz");
			try {
				PackageInfo pInfo = con.getPackageManager().getPackageInfo(
						con.getPackageName(), PackageManager.GET_META_DATA);
				int version = pInfo.versionCode;

				String url = "";
				if (v > version && m == 1) {
					try {
						// Check whether Google Play store is installed or not:
						con.getPackageManager().getPackageInfo(
								"com.android.vending", 0);
						// Toast.makeText(con,
						// "Please update application to continue using service",Toast.LENGTH_LONG).show();
						url = "market://details?id=" + con.getPackageName();
					} catch (final Exception e) {
						url = "https://play.google.com/store/apps/details?id="
								+ con.getPackageName();
					}

					// Open the application page in Google Play store:
					final Intent intent = new Intent(Intent.ACTION_VIEW,
							Uri.parse(url));
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
					con.startActivity(intent);
					return 0;

				} else if (v > version)
					return 1;

			} catch (NameNotFoundException e1) {

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 2;
	}

	public int changehighscore(int sc, int x, int y, int z) {

		JSONObject b = null;
		Calendar c = Calendar.getInstance();
		int day = c.get(Calendar.DAY_OF_MONTH);

		try {
			b = new JSONObject(getdata("ghjk"));

			if (sc > b.getInt("score") || b.getInt("day") != day) {
				b.put("token", getdata("asdf"));
				b.put("score", sc);
				b.put("day", day);
				java.util.Date date = new java.util.Date();
				b.put("ts", new Timestamp(date.getTime()));
				b.put("x", x);
				b.put("y", y);
				b.put("z", z);
				putdata("ghjk", b.toString());

				new SendData(b.toString(),
						"http://cetdhwani.com/bottlerun/update.php", 2, con)
						.execute();

				return 1;
			} else {
				return 0;
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			 b = new JSONObject();
			 try
			 {
			b.put("token", getdata("asdf"));
			b.put("score", sc);
			b.put("day", day);
			java.util.Date date = new java.util.Date();
			b.put("ts", new Timestamp(date.getTime()));
			b.put("x", x);
			b.put("y", y);
			b.put("z", z);
			putdata("ghjk", b.toString());

			new SendData(b.toString(),
					"http://cetdhwani.com/bottlerun/update.php", 2, con)
					.execute();
			 }
			 catch(Exception e1){}
		}

		return 0;
	}

	public ArrayAdapter<String> getColleges() {
		String colleges = "[\"CET \",\"Alphonsa College, Pala \",\"Acquinas College, Edacochin\",\"Adhiyamaan College Of Engineering,Hosur\",\"Adi Sankara Institute of Engg. \",\"Al-Ameen College of Engineering, Pattambi\",\"Al-ameen College, Edathala\",\"Alberts Institute of Management\",\"All Saints College,Trivandrum\",\"Amal Jyothi Engineering College, Kanjirapally \",\"Amala Institute of Medical Sciences, Thrissur\",\"Amrita Vishwa Vidyapeetham, Coimbatore.\",\"Amrita Vishwa Vidyapeetham,Kollam \",\"anglo eastern maritime academy\",\"Annoor Dental College, Puthupaddy P.O, Ernakulam\",\"Ansar Arabic College,Valavannur\",\"Anvarul Islam Arabic College, Kuniel Areacode, Malappuram\",\"Anvarul Islam Women Arabic College, Mongam, Malappuram\",\"Assumption College, Changancherry\",\"Avinashilingam University for Women, Coimbatore\",\"AWH Engineering College, Kuttikatoor, Kozhikkode\",\"B.A.M.College, Thuruthicaud\",\"B.K.College for Women, Kottayam\",\"Baselios Mathew II College of Engg., Sasthamcotta\",\"Baselius College, Kottayam\",\"Baselius Poulose II Catholicate College, Piravam\",\"Bharatha matha College, Thrikkakara\",\"Bishop Choolaparambil Memorial College, Kottayam\",\"Bishop Moore College, Mavelikkara\",\"C. K .Govindan Nair Memorial Government College, Perambra\",\"C.M.S.College, Kottayam\",\"Calicut University Institute of Engg. and Tech , Kohinoor\",\"Canara Engineering College,Mangalore\",\"Carmel College, Mala\",\"Carmel Engineering College, Koonamkara, Ranni\",\"Catholicate College, Pathanamthitta\",\"CENTUARY DENTAL COLLEGE\",\"Chembai Memorial Music College, Palakkad\",\"chengannur engineering college\",\"Christ College, Irinjalakuda\",\"Christian College ,Kattakada\",\"Christian College, Chengannur\",\"Co-Operative Arts and Science College, Madayi\",\"Co-operative Institute of Technology, Mandarathur\",\"Co-operative Medical College, Cochin\",\"Cochin College, Cochin\",\"Cochin University Of Science And Technology\",\"College of Applied Science Pattuvam\",\"College of Dairy Science and Technology, Mannuthy\",\"College of Engineering Eranjoli P.O, Thalassery, Kannur\",\"College of Engineering Kidangoor, Kidangoor South\",\"College of Engineering Perumon, Perinad\",\"College of Engineering Thiruvananthapuram\",\"College of Engineering Trikaripur, Cheemeni\",\"College of Engineering, Adoor\",\"College of Engineering, Attingal, Thiruvananthapuram\",\"College of Engineering, Chengannur P.O, Alappuzha\",\"College of Engineering, County Hills Munnar\",\"College of Engineering, Kallooppara, Kadamankulam\",\"College of Engineering, Karunagapally, Pada.North\",\"College of Engineering, Pallippuram\",\"College of Engineering, Thekkekara P.O, Poonjar\",\"College of Engineering, Thrikkannamangal,Kottarakara\",\"college of fine arts, palayam\",\"College of Teacher Education Kozhikode\",\"College of Teacher Education, Thalasserry\",\"College of Teacher Education,Thiruvananthapuram\",\"college ofengineering,kottarakara\",\"D. B. College, Thalayolaparambu\",\"Darul Aloom Arabic College, Paral, Thalassery\",\"Darul Irshad Arabic College, Paral, Kannur\",\"Darunnajith Arabic College, Karuvarakundu\",\"decaying humanity\",\"Devamatha College. Kuravilangad\",\"Devaswom Board College, Parumala, Pamba\",\"Devaswom Board College, Sasthamcotta Kollam\",\"Don Bosco College,Kannur\",\"Don Bosco College,Trichur\",\"Dr. Somervell Memorial CSI Medical College, Karakonam\",\"dutch street\",\"EMEA College, Kondotty\",\"ER\",\"ex CET\",\"Farook College, Farook\",\"Farook Training College, Farook\",\"Fathima Matha National College, Kollam\",\"Federal Institute of Science \",\"Fr. Muller Medical College, Mangalore\",\"Goverment Dental College, Thiruvananthapuram\",\"Government Ayurveda College, Trivandrum\",\"Government College of Engineering Mangattuparamba\",\"Government Dental College, Kottayam\",\"Government Dental College, Kozhikode\",\"Government Engineering College Kozhikode, West Hill\",\"Government Engineering College Painavu, Idukki\",\"Government Engineering College Palakkad, Sreekrishnapuram\",\"Government Engineering College Thrissur, R V Puram\",\"Government Engineering College Wayanad, Nalloornad\",\"Government Engineering College, Barton Hill\",\"Government Rajiv Gandhi Institute of Technology, Vellore\",\"Government Sanskrit College, Pattambi\",\"Government Victoria College, Palakkad\",\"Govt. Arts \",\"Govt. Brennen College,Thalassery\",\"Govt. College for Women, Trivandrum\",\"Govt. College, Kariavattom,Trivandrum\",\"Govt. Homoeopathic Medical College, Calicut\",\"Govt. Law College, Kozhikode\",\"Govt. Medical College, Alappuzha\",\"Govt. Medical College, Kottayam\",\"Govt. Medical College, Kozhikode\",\"Govt. Medical College, Thiruvananthapuram\",\"Govt. Medical College, Thrissur\",\"Govt. Sanskrit College,Trivandrum\",\"Govt.Arts College, Trivandrum\",\"Henry Baker College, Melukavu\",\"I E S College of Engineering, Thrissur\",\"IHMCT ,kovlam\",\"IIM Calcutta\",\"iim kozhikode\",\"IISER Trivandrum\",\"IIST\",\"IIT,Madras\",\"Ilahia College of Engineering \",\"Institute of Advanced Study in Education, Thrissur\",\"Iqbal College,Peringamala\",\"Jeppiaar Engineering College,Chennai\",\"jerichoend\",\"jerichos end\",\"JNTU,Kakinada\",\"Jubilee Mission Medical College \",\"Jyothi Engineering College, Cheruthuruthy\",\"K .E. College, Mannanam\",\"K M C T College of Engineering Kozhikode\",\"K M E A Engineering College, Alwaye\",\"K.L.N College of Engineering\",\"K.V.V.S.COLLEGE OF SCIENCE 7 TECHNOLOGY\",\"Karmala Rani Training College, Kollam\",\"Karuna Medical College,Palakkad\",\"Karunya University,Coibathur\",\"kasturba medical college , mangalore\",\"Kelappaji College of Agricultural Engineering and Technology\",\"Keyi Sahib Training College Taliparamba\",\"KKTM Govt College,Pullut\",\"KMCT Medical College,Kozhikode\",\"kmm college of arts and science\",\"kraft labs\",\"Krishna Menon Memorial Women`s College, Kannur\",\"Kuriakose Gregrios College, Pampady, Kottayam\",\"L.B.S College of Engineering, Kasaragod, Povval, Muliyar\",\"L.B.S Institute of Technology for Women, Poojapura\",\"Little Flower College Guruvayoor\",\"Lord Jegannath College of engg\",\"Lourdes Matha College of Science \",\"Loyola College of Social Sciences,Trivandrum\",\"M E A Engineering College, Chemmaniyod, Malappuram\",\"M E S College of Engineering, Kuttippuram\",\"M G College of Engineering, Thiruvallom\",\"M. G. College,Thiruvananthapuram\",\"M.G.College, Iritty\",\"M.G.U.C.E,muttom\",\"Maamallan Institute of Technology\",\"MACFAST,Thiruvalla\",\"Madeerathul Uloom Arabic College, Pulikkal, Malappuram\",\"Maharajas College, Ernakulam\",\"Mahatma Gandhi University College of Engineering, Muttom\",\"Malabar Christian College\",\"Malabar Christian College, Kozhikode\",\"Mangalam College of Engineering, Ettumanoor\",\"Mannam Memorial NSS College,Kottiyam, Kollam\",\"Mannaniya College of Arts \",\"Mar Athanasius Arts and Science college,kothamangalam\",\"Mar Athanasius College of Engineering , Kothamangalam\",\"Mar Baselios College of Engineering \",\"Mar Dianysius College, Pazhanji\",\"Mar Ephraem College, Marthandam\",\"Mar Ivanios College, Thiruvananthapuram\",\"Mar Thoma College for Women, Perumbavoor\",\"Mar Thoma College, Chungathara\",\"Marian College, Peerumade\",\"Marian Engineering College, Kazhakuttom\",\"Martheophilus Training College, Bathanihills\",\"Marthoma College, Thiruvalla\",\"Mary Matha Arts and Science College, Mananthavady\",\"Mary Matha College of Engineering, Olathanni\",\"Matha College of Technology, Manakappadi\",\"MEA Sullamussalam Science College, Areacode, Malappuram\",\"Medical College, Trivandrum\",\"Mercy College, Palakkad\",\"MES Asmabi College, Vemballur\",\"MES College, Ponnani\",\"MES kuttiippuram,thrikkannapuram\",\"MES KVM College, Valancherry\",\"MES Mampad College, Mampad\",\"MES Medical College, Perinthalmanna\",\"MGM College\",\"Miladi sherif Memorial College, Kayamkulam\",\"Model Engineering College, B.M.C Post, Thrikkaara\",\"Mohandas College of Engg. \",\"Morning Star Home Science College, Angamaly\",\"MOSC Medical College, Kolenchery\",\"Mount Carmel Training College, Kottayam\",\"Mount Tabour Training College, Pathanapuram\",\"Mount Zion College of Engg., Pathanamthitta\",\"Muhammed Adburahiman Memorial Orphange College, Mannassery\",\"Musaliar College of Engineering \",\"Muslim Association College of Engineering, Venjaramoodu\",\"Muslim Education Society Kalladi College, Mannarghat\",\"Muslim Educational Society College, Marampally\",\"Muslim Educational Society College, Nedumkandam\",\"N.S.S College of Engineering, Palakkad\",\"Nangelil Ayurveda Medical College,Ernakulam\",\"Nasarathul Islam Arabic College, Kadavanthara\",\"national university of advanced legel studies \",\"Nehru Arts and Science College, Kanhangad\",\"Nehru College of Engg. \",\"Newman College , Thodupuzha\",\"Nirmala College, Muvattupuzha\",\"Nirmalagiri College, Koothuparambu\",\"NIT, Calicut\",\"NIT,Surathkal\",\"NIT,Trichy\",\"NIT,Warangal\",\"NMAMIT,Karnataka\",\"Noorul Islam College of Dental Science, Trivandrum\",\"NSS College for Women, Thiruvananthapuram\",\"NSS College of Engineering,Palakkad\",\"NSS College, Cherthala\",\"NSS College, Manjeri\",\"NSS College, Nemmara\",\"NSS College, Nilamel. Kollam\",\"NSS College, Ottapalam\",\"NSS College, Pandalam\",\"NSS College, Rajakumari\",\"NSS Hindu College, Changanacherry\",\"NSS Training College, Changanacherry\",\"NSS Training College, Ottapalam\",\"NSS Training College, Pandalam\",\"others\",\"P A Aziz College of Engineering \",\"P.K.M.College of Education\",\"P.T.M Govt. College,Perinthalmanna\",\"Pankajakasthuri CET\",\"Pariyaram Medical College, Kannur\",\"Pavanatma College, Murickassery\",\"Payyannur College, Payyannur\",\"Pazhassi raja NSS College, Mattannur\",\"Pazhassiraja College, Pulpally\",\"Peet Memorial Training College, Mavelikkara\",\"Physical Education College East Hill, Kozhikode\",\"pondicherry engineering college\",\"Prajyothi Nikethan College, Puthukkad, Thrissur\",\"Providence Women`s College, Kozhikode\",\"PSG College Of Arts \",\"PSMO College, Thirurangadi\",\"Pushpagiri College of Dental Sciences, Thiruvalla\",\"R K M Vivekananda College,Chennai\",\"rajadhani institute of eng. \",\"Rajagiri College of Social Science, Kalamassery\",\"Rajagiri School of Engg. \",\"Rajalakshmi Engineering College,Chennai\",\"Rajiv Gandhi Institute of Technology, Kottayam\",\"requiem\",\"Ronsathal Uloom Arabic College, Farook\",\"roses for the dead\",\"Royal College of Engineering \",\"Royal Dental College,Palakkad\",\"S .N College, Punalur, Kollam\",\"S R M University, Kattankulathur\",\"S. E. S. College, Sreekandapuram\",\"S. N. College for Women, Kollam\",\"S. N. College, Alathur\",\"S. N. College, Chathannoor, Kollam\",\"S. N. College, Chempazhanthy\",\"S. N. College, Chengannur\",\"S. N. College, Cherthala\",\"S. N. College, Nattika\",\"S. N. College, Sivagiri,Varkala\",\"S. N. College, Thottada, Kannur\",\"S. N. Guru College, Chelannur\",\"S. N. Training College, Nedumganda\",\"S. N.College, Kollam\",\"S. N.M.College, Maliankara\",\"S.I.T ,Madurai,Tamilnadu\",\"S.N College alathur,Palakad\",\"Sacred Heart College, Chalakudy\",\"Sacred Heart College, Thevara\",\"SACSMAVMM\",\"SAE College, Chennai\",\"Sahodaran Ayyappan Smaraka(SNDP)Yogam College, Konni\",\"Sahrdaya College of Engineering \",\"Saintgits College of Engineering, Pathamuttom\",\"Sanatana Dharma College, Alappuzha\",\"Sarabhai Institute of Science \",\"school of architecture,nasik\",\"school of engineering,cusat\",\"School of Management Studies, CUSAT\",\"SCMS School of Engg. \",\"SCT,CHALAKKUDI\",\"Shahul Hameed Memorial Engineering College, Kadakkal\",\"siena college of professional studies\",\"Sir Syed College, Thaliparamba\",\"SMU manipal\",\"SOE,cusat\",\"sree\",\"Sree Buddha College of Engineering, Pattoor\",\"Sree Chitra Thirunal College of Engineering, Pappanamcode\",\"Sree Gokulam Medical College \",\"Sree Kerala Varma College, Thrissur\",\"Sree Krishna College, Thrissur\",\"Sree Narayana Guru College of Engineering \",\"Sree Narayana Gurukulam College of Engineering, Kadayiruppu\",\"Sree Narayana Mangalam Inst. of Mgmt. \",\"Sree Narayana Mangalam Training College, Moothakunnam\",\"Sree Narayana Trust College, Shoranur\",\"sree narayanacollege,alathur\",\"Sree Sankara College, Kalady\",\"Sree Swathi Thirunal College of Music Thiruvananthapuram\",\"Sree Vivekananda College, Kizhoor\",\"Sree Vyasa NSS College, wadakkancherry\",\"Sri Venkateswara College of Engineering, Chennai\",\"Sri. Ayyappa Devaswom Board College, Eramalikaraa\",\"Sri. Sankara Dental College,Varkala.\",\"Sri.Vidyadiraja NSS College, Vazhoor\",\"Srinivas Institute of Management Studies,Mangalore\",\"SSV.College, Perumbavoor\",\"St. Aloysius College, Edathua\",\"St. Berchmans College, Changanacherry\",\"St. Cyrils College, Adoor\",\"St. Dominic College, Kanjirappally\",\"St. George College, Aruvithura\",\"St. Gregorios College, Kottarakara\",\"St. Gregorios Dental College,Kothamangalam\",\"St. Johns College Anchal\",\"St. Joseph College for Women, Alappuzha\",\"St. Joseph Training College for Women, Ernakulam\",\"St. Joseph Training College, Mannanam, Kottayam\",\"St. Josephs College, Devagiri\",\"St. Josephs College, Irinjalakuda\",\"St. Josephs College, Moolamattom\",\"St. Joseph`s College of Engg. \",\"St. Mary`s College, Sulthan bathery\",\"St. Michaels College, Cherthala\",\"St. Stephen College, Pathanapuram\",\"St. Stephens College, Uzhavoor\",\"St. Theresas College, Ernakulam\",\"St. Thomas College, Kozhencherry\",\"St. Thomas College, Ranni\",\"St. Thomas Training College, Palai\",\"St. Xaviers College for Women, Aluva\",\"St. Xaviers College, Kothavara, Vaikom\",\"St. Xaviers College, Thiruvananthapuram\",\"St.Alberts College, Ernakulam\",\"St.Aloysius College, Elthuruthu\",\"St.Marys College, Manarcadu\",\"St.Marys College, Thrissur\",\"St.Peters College, Kolencherry\",\"St.Pius College, Rajapuram\",\"St.Thoma College, Thrissur\",\"St.Thomas College, Pala\",\"Sullamussalam Arabic College, Areacode\",\"Sunnivva Arabic College, Chennamangallur\",\"SUT Medical College, Trivandrum\",\"T. K. M Institute of Technology, Ezhukone\",\"T.K.Madhavan Memorial College, Nangiarkulangara\",\"Thangal Kunju Memorial Arts and science College, Kollam\",\"The national university of advanced legal studies, Kochi\",\"Titus II Teachers Colleges Thiruvalla\",\"TKM College of Engineering, TKM College Post, Kollam\",\"Toc H Institute of Science \",\"Travancore Engineering College, Aayoor\",\"UIT\",\"Union Christian College, Aluva\",\"Unity women`s College, Manjeri\",\"University College of Engg, Kariavattom, TVM\",\"V.L.B JANAKIAMMAL COLLEGE OF ENGG.\",\"Veda Vyasa Institute of Technology, Ponnempadam\",\"Vidya Academy of Science \",\"VIDYODAYA Pre University College,Udupi\",\"Vimal Jyothi Engineering College, Chemperi\",\"Vimala College, Thrissur\",\"Viswajyothi College of Engineering \",\"void\",\"VTB College, Mannampatta\",\"VTM NSS College,Dhanuvachapuram\",\"W. M. O. Arts and Science College, Muttil\",\"Younus College of Engineering \",\"Zamorins Guruvayurappan College, Kozhikode\",\"zeidgeist\",\"NIL\",\"m g university college of engineering\",\"m g university college of engineering\",\"M G UNIVERSITY\",\"CENTURY DENTAL COLLEGE KASARGOD\",\"KERALA LAW ACADEMY LAW COLLEGE, Perurkada\",\"IIITMK TECNOPARK\",\"UNIVERSITY C\",\"MG College Of Engineering\",\"PRS College of engg\",\"st thomas institute of scince and technology\",\"T.D MEDICAL COLLEGE,ALAPUZHA\",\"ST. THOMAS INSTITUTE OF SCIENCE AND TECHNOLOGY\",\"Fisat\",\"Sathyabama University\",\"UNIVERSITY OF PETROLEUM AND ENERGY STUDIES, DEHRA DUN\",\"HEERA COLLEGE OF ENGINEERING\",\"BHAWANIPORE EDUCATION SOCEITY KOLKATA \",\"cochin university college of engineering kuttanad\",\"NUALS KOCHI\",\"St Thomas College, Palai\",\"LOYOLA INSTITUTE OF TECHNOLOGY&SCIENCE\",\"Loyola Institue Of Technology and Science, Thovala\",\"LOYOLA INSTITUTE OF TECHNOLOGY AND SCIENCE\",\"INSTITUTE OF MANAGEMENT,KERALA\",\"Mar Dioscorus college of pharmacy,sreekaryam\",\"GOVT LAW COLLEGE ERNAKULAM\",\"sastra university\",\"Sree budha college of engineering\",\"ERODE SENGUNTHAR ENGINEERING COLLEGE ,TAMILNADU\",\"Govt. Law College, TVM\",\"CENTRALPOLYTECHNICCOLLEGE TVM\",\"RGNUL Punjab\",\"RL INS,MADURAI\",\"MBCET, NALANCHIRA\",\"Mar Baselios College of Engineering,Idukki\",\"Vidya College of Engineering\",\"MANIPAL INSTITUTE OF TECHNOLOGY\",\"NOORUL ISLAM POLYTECHINC COLLEGE\",\"COLLEGE OF ARCHITECTURE TRIVANDRUM\",\"O C ET\",\"Vellore Institute of Technology\",\"C M S DENTAL COLLEGE\",\"MES COLLEGE marampally\",\"SMCSI MEDICAL COLLEGE, KARAKONAM\",\"SN COLLEGE CHEMPAZHANTHY\",\"GOVERNMENT COLLEGE KASARAGOD\",\"Nehru Arts and Science College Kanhangad\",\"Albertian Institute of science and technology Kalamassery\",\"Andra University College of Engineering Vishakapatanam\",\"John Cox Memorial ,Trivandrum\",\"Bishop Jerome Institute of Engineering and Technology , Kollam \",\"asan memorial college of arts and science\",\"Thangal Kunju Musaliar Institute of Technology\",\"Coimbatore Institute of Engineering and Technology\",\"KMCT college of engineering\",\"r v college of engineering, bangalore\",\"dmi engg colg kumarapuram\",\"cape institute of technology levengipuram\",\"cape institute of technology levengipuram\",\"Ahalia School Of Engineering Technology, Palakkad\",\"M. KUMARASAMY college of engineering, karur\",\"bannari amman institute of technology\",\"SRM Chennai\",\"government arts college ,trivandrum\",\"MEPCO Schlenk engineering college,Sivakasi\",\"college of engineering and management ,alappuzha\",\"St.josephs college pala\",\"vardhaman college of engineering\",\"velammal engineering college\",\"Rajiv Gandhi College Of Engineering And Technology, Pudhucherry\",\"Vel Tech University, Chennai\",\"Vel Tech University, Chennai\",\"Vel Tech University, Chennai\",\"Vel Tech University, Chennai\",\"MES ENGINEERING COLLEGE PERINTHALMANA\",\"college of engineering vadakara\",\"COLLEGE OF ENGINEERING & MANAGEMENT PUNNAPRA\",\"Sri Eshwar college of engineering coimbathor\",\"sri shakthi institute of engineering and technology, chennai\",\"MEA Engineering College, Perinhalmanna\",\"shri devi institute of engineering and technology\"]";

		JSONArray collList;
		try {
			collList = new JSONArray(colleges);
			ArrayList<String> names_colleges = new ArrayList<String>();
			for (int i = 0; i < collList.length(); i++) {
				names_colleges.add(collList.getString(i));
			}

			ArrayAdapter<String> adp = new ArrayAdapter<String>(con,
					android.R.layout.simple_dropdown_item_1line, names_colleges);
			return adp;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		}

		return null;
	}

}
