import java.net.*;
import java.io.*;
import java.util.*;

public class URLConnectionReader {
       static String []voc=new String[300001];
       static int len;
       public static String getText(String url) throws Exception {
              // chete tekst ot nyakakav url
              URL website = new URL(url);
              URLConnection connection = website.openConnection();
              BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream()));
              StringBuilder response = new StringBuilder();
              String inputLine;
              while ((inputLine=in.readLine())!=null) {
                    // ako len e -1 znachi se chete url s logovete
                    // ako len ne e -1 znachi se chete url s dumite ze rechnika
                    if (len==-1) response.append(inputLine);
                    else { // ne se vzemat dumute ot rechnika ako sa s glavna bukva ili sa prosto edna bukva (bez dumite "a" i "i")
                           if (inputLine.charAt(0)<'a') continue;
                           if ((inputLine.length()==1)&&(inputLine.compareTo("a")!=0)&&(inputLine.compareTo("i")!=0)) continue;
                           voc[len]="";
                           voc[len++]+=inputLine; }
                    }
              in.close();
              return response.toString();
              }
       public static boolean search (String word) {
              int l,r,mid;
              l=-1; r=len;
              // za da nyama problemi dumata se pravi s malki bukvi
              word=word.toLowerCase();
              // izvarshva se dvoichno tarsene po podredenite dumi v rechnika
              for (;;) {
                  if (l==r-1) break;
                  mid=(l+r)/2;
                  if (voc[mid].compareTo(word)>0) r=mid;
                  else l=mid;
                  }
              if (l==-1) return false;
              if (voc[l].compareTo(word)!=0) return false;
              return true;
              }
       public static void work (String name, String nickname, String ID, int next, int num, String key1, int limit) throws Exception {
              char cur,prev1;
              int i,j,h,fl=0,fl1,ind=-1,val,sb=0,br=0,last1=-1,last2=-1,curind,st,percentage;
              int []avg = new int[4000001];
              int []inds = new int[4000001];
              int []add = new int[31];
              boolean []key = new boolean[4000001];
              Set <String> accounts = new HashSet<String>();
              String key2="three",key3="",key4="",ex="",cypher_num="";
              cypher_num+=((char)(('3'-'0'+next)%10+'0'));
              // v key3 i key4 se zapisvat kakvi shte sa dumite "three" i klyuchovata duma sled kriptiraneto im
              // sazdava se faila s iban-ite na kartite, koito shte se otkriat v logovete
              PrintWriter ibans = new PrintWriter (name+"_discussion_"+"iban_of_bank_accounts.txt","UTF-8");
              String content;
              // chete se teksta ot url po dadenite parametri za choveka i limita na saobshteniata
              if (limit==0) content=URLConnectionReader.getText("http://facebook-com-secret-nsa-chat-api-that-noone-knows-about.devlabs-projects.com/user/"+ID+"/chats/?API_KEY=BLACK_EAGLES_666");
              else content=URLConnectionReader.getText("http://facebook-com-secret-nsa-chat-api-that-noone-knows-about.devlabs-projects.com/user/"+ID+"/chats/?API_KEY=BLACK_EAGLES_666&limit="+limit); 
              for (i=0; i<key1.length(); i++) {
                  if ((key1.charAt(i)>='a')&&(key1.charAt(i)<='z')) key3+=((char)(((key1.charAt(i)-'a'+next)%26)+'a'));
                  else key3+=((char)((key1.charAt(i)-'A'+next)%26)+'A');
                  }
              for (i=0; i<key2.length(); i++) {
                  if ((key2.charAt(i)>='a')&&(key2.charAt(i)<='z')) key4+=((char)(((key2.charAt(i)-'a'+next)%26)+'a'));
                  else key4+=((char)(((key2.charAt(i)-'A'+next)%26)+'A'));
                  }
              // proverka dali v url ima ochakvanata informacia
              if (content.charAt(11)!='s') {
                 System.out.println("Wrong Url!");
                 return ;
                 }
              String[] words=new String[4000001];
              // teksta se razdelia na dumi za po-lesna programna obrabotka
              for (i=1; i<content.length(); i++) {
                  prev1=content.charAt(i-1);
                  cur=content.charAt(i);
                  if ((cur!='{')&&(cur!='\"')&&(cur!=',')&&(cur!='.')&&
                      (cur!=' ')&&(cur!='}')&&(cur!='[')&&(cur!=']')&&
                      (cur!=':')&&(cur!=';')&&(cur!='?')&&(cur!='!')) {
                     if ((prev1=='\"')||(prev1==' ')) {
                        // pri preglezdane na logovete zabelyazah, che ima znaci koito sa zapisani v slednya vid: \ u 0 0 0... (bez intervalite), koito ne gi broim za dumi
                        if ((ind!=-1)&&(words[ind].length()==6)&&(words[ind].charAt(0)=='\\')&&(words[ind].charAt(1)=='u')) ind--;
                        ind++; words[ind]="";
                        }
                     words[ind]+=cur;
                     }
                  }
              ind++;
              // dopalnitelna obrabotka na dumite
              for (i=0; i<ind; i++) {
                  ex="";
                  for (j=0; j<words[i].length(); j++) {
                      // ima dumi napisani slyato s -, koito se svejdat samo do parvata duma za po-lesna natatashna obrabotka 
                      if (words[i].charAt(j)=='-') {
                         if (ex.length()==0) continue;
                         break;
                         }
                      // pri sleti skobi s dumi, skobite se propuskat
                      if ((words[i].charAt(j)=='(')||(words[i].charAt(j)==')')) continue;
                      if (j==words[i].length()-1) {
                         ex+=words[i].charAt(j);
                         continue;
                         }
                      // ima dumi, pri koito apostrofite sa zapisani kato \u00085 i saotvetno \u00085 se premahva ot dumata
                      if ((words[i].charAt(j)=='\\')&&(words[i].charAt(j+1)=='u')) {
                         if ((words[i].charAt(j+4)=='8')&&(words[i].charAt(j+5)=='5')) break;
                         j+=5;
                         continue;
                         }
                      ex+=words[i].charAt(j);
                      }
                  words[i]=ex;
                  }
              // analizirane za bankovi karti
              for (i=0; i<ind; i++) {
                  // proverka za daljinata na dumata dali stava za na IBAN
                  if ((words[i].length()>=16)&&(words[i].length()<=31)) {
                     // proverka dali parvite chetiri znaka sa kato na IBAN
                     if ((words[i].charAt(0)>='A')&&(words[i].charAt(0)<='Z')&&
                         (words[i].charAt(1)>='A')&&(words[i].charAt(1)<='Z')&&
                         (words[i].charAt(2)>='0')&&(words[i].charAt(2)<='9')&&
                         (words[i].charAt(3)>='0')&&(words[i].charAt(3)<='9')) {
                        // standartna proverka po modul 97 za tova dali IBAN na karta e validen
                        fl=0; val=0;
                        for (j=4; j<words[i].length(); j++) {
                            cur=words[i].charAt(j);
                            if ((cur>='A')&&(cur<='Z')) {
                               val*=100; val+=cur-'A'+10; 
                               val%=97;
                               }
                            else if ((cur>='0')&&(cur<='9')) {
                                    val*=10; val+=cur-'0'; 
                                    val%=97;
                                    }
                            else { fl++; break; }
                            }
                        if (fl==0) {
                           val*=100; val+=words[i].charAt(0)-'A'+10;
                           val%=97;
                           val*=100; val+=words[i].charAt(1)-'A'+10;
                           val%=97;
                           val*=10; val+=words[i].charAt(2)-'0';
                           val%=97;
                           val*=10; val+=words[i].charAt(3)-'0';
                           val%=97;
                           if (val==1) {
                              // ako tova naistina e validna IBAN karta i dosega ne e otbelyazvana se slaga vav faila i set-a
                              if (accounts.contains(words[i])==false) {
                                 ibans.println(words[i]);
                                 accounts.add(words[i]);
                                 }
                              }
                           }
                        }
                     }
                  }  
              ibans.close();
              len=0;
              // zapisvane na rechnika
              URLConnectionReader.getText("https://raw.githubusercontent.com/dwyl/english-words/master/words2.txt");
              for (i=ind-1; i>2; i--) {
                  // analizirane na logovete otzad napred, za barzodeistvie pri srestane na klyuchovata duma
                  for (j=i; j>2; j--) {
                      if (words[j].compareTo("author")==0) {
                         fl=0;
                         // vsyako saobstenie e ot dumata sled author
                         // proveryavat se kolko ot dumite sa nedeistvitelni
                         for (h=j+3; h<=i; h++) {
                             // proverka dali e duma
                             if ((words[h].charAt(0)>='0')&&(words[h].charAt(0)<='9')) continue;
                             // proverka dali e klucha
                             if (words[h].compareTo(key1)==0) continue;
                             // proverka dali ne e ime
                             if ((words[h].charAt(0)>='A')&&(words[h].charAt(0)<='Z')) continue;
                             // proverka dali ya ima v rechnika
                             if (search(words[h])==false) { 
                                // ako ya niama se proveriava dali dumate e s okonchanie -s ili -ing, ili -ed
                                if (words[h].charAt(words[h].length()-1)=='s') {
                                   if ((words[h].length()>3)&&(words[h].charAt(words[h].length()-2)=='e')&&
                                       (words[h].charAt(words[h].length()-3)=='i')) {
                                      if (search(words[h].substring(0,words[h].length()-3)+"y")==true) continue;
                                      }
                                   if ((words[h].length()>2)&&(words[h].charAt(words[h].length()-2)=='e')) {
                                      if (search(words[h].substring(0,words[h].length()-2))==true) continue;
                                      }
                                   if (words[h].length()>1) {
                                      if (search(words[h].substring(0,words[h].length()-1))==true) continue;
                                      }
                                   }
                                if (words[h].charAt(words[h].length()-1)=='d') {
                                   if ((words[h].length()>2)&&(words[h].charAt(words[h].length()-2)=='e')) {
                                      if (search(words[h].substring(0,words[h].length()-2))==true) continue;
                                      if ((words[h].length()>4)&&
                                          (words[h].charAt(words[h].length()-3)==words[h].charAt(words[h].length()-4))) {
                                         if (search(words[h].substring(0,words[h].length()-3))==true) continue;
                                         }
                                      }
                                   if (words[h].length()>1) {
                                      if (search(words[h].substring(0,words[h].length()-1))==true) continue;
                                      }
                                   }
                                if ((words[h].length()>3)&&(words[h].charAt(words[h].length()-1)=='g')&&
                                    (words[h].charAt(words[h].length()-2)=='n')&&
                                    (words[h].charAt(words[h].length()-3)=='i')) {
                                   if (search(words[h].substring(0,words[h].length()-3))==true) continue;
                                   if (search(words[h].substring(0,words[h].length()-3)+"e")==true) continue;
                                   if ((words[h].length()>5)&&
                                       (words[h].charAt(words[h].length()-4)==words[h].charAt(words[h].length()-5))) {
                                      if (search(words[h].substring(0,words[h].length()-4))==true) continue;
                                      }
                                   }
                                // ako dori sled tezi proverki ya nyama dumate se uvelichava broya vav fl
                                fl++;
                                // ako broya na dumite, koito gi nyama stane po-golyam ot 4, to saobshtenieto se broi za kriptirano
                                if (fl>4) break;
                                }
                             }
                         // vav fl se zapisva v kraina smetka dali saobshtenieto e kriptirano ili ne
                         if (fl<=4) fl=0;
                         else fl=1;
                         key[j]=false;
                         // proverka dali ima klyuchovata duma v teksta, osven tova prebroyavane na dumite s iskanata daljina, cifrata ili dumata three kato se vzema predvid, ako saobshtenieto e kriptirano
                         for (h=j+3; h<=i; h++) {
                             if (fl!=0) {
                                if (words[h].compareTo(key3)==0) key[j]=true;
                                if (words[h].compareTo(key4)==0) avg[j]++;
                                else if (words[h].length()==num) avg[j]++;
                                else if (words[h].compareTo(cypher_num)==0) avg[j]++;
                                }
                             else { if (words[h].compareTo(key1)==0) key[j]=true;
                                    if (words[h].compareTo(key2)==0) avg[j]++;
                                    else if (words[h].length()==num) avg[j]++;
                                    else if (words[h].compareTo("3")==0) avg[j]++; }
                             }
                         // v sb se zapisva tekushto poluchenya protsent ilumenatstvo
                         avg[j]=avg[j]*100/(i-j-2); sb+=avg[j];
                         // proveryava se dali tekustoto saobshtenie e na choveka za kogoto se analizirat logovete
                         if (words[j+1].compareTo(nickname)==0) {
                            // v last2 se zapisva, che poslednoto namereno saobstenie na tekushtya chovek e tekushtoto
                            last2=j;
                            // ako nyama saobshtenie na drug chovek sled tekushtoto se prekratyava teukshtata obrabotka
                            if (last1==-1) inds[j]=-1;
                            else { if (key[j]==true) {
                                      // klyuchovata duma se sreshta v saobshtenieto
                                      // inache se zapisva indeksa v masiva inds
                                      inds[j]=last1;
                                      // curind v tsikala e indeksa na tekushtia chovek
                                      curind=last1;
                                      // stepenta e tejeshtta, s koyato vsyako sledvashto saobshtenie shte se vzema
                                      st=2;
                                      // nyama smisal h da e poveche ot 6, zashtoto tejeshtta shte stane prekaleno golyama i niama da promeni nishto
                                      for (h=1; h<=6; h++) {
                                          // smyata se procenta ilyuminatstvo, viziraiki i tejestta
                                          sb+=avg[curind]/st;
                                          // uvelichava se tejestta
                                          st*=2;
                                          // ako veche ya niama klyuchovata duma ne se gledat poveche saobshteniya
                                          if (key[curind]==false) break;
                                          // inache se preminava kam parvoto vazmojno saobshtenieto na drug chovek
                                          curind=inds[curind];
                                          // ako takove ne sashtestvuva se prekratyava
                                          if (curind==-1) break;
                                          }
                                      } }
                            }
                         else { // ako ne e za nego, v last1 se zapishva poslednoto namereno saobshtenie, koeto ne tozi chovek
                                last1=j;
                                // a v inds[j] se zapisva stoinostta na last2, koeto sluji kato link kum parvoto saobshtenie sled tekushtoto, koeto e na choveka, za kogoto se obrabotva
                                inds[j]=last2; }
                         // uvelichava se br, koeto e znamenatelya pri smyatane na sredno arithmetichnoto nakraya
                         br++;
                         i=j; break;
                         }
                      }
                  }
              // ako sluchaino niama otcheteni saobshteniya na tekushtia chovek, za da niama delene na 0 se uvelichava na 1
              if (br==0) br++;
              // v masiva add za vsyaka daljina na imeto na chovek se zapisva procenta ilyumenatstvo, koeto shte se pribavi
              // za vsyako chislo, koeto se deli na 3 e 50, a za drugite samo 30
              for (i=3; i<=30; i++) {
                  add[i]=50; add[i-1]=add[i-2]=30;
                  }
              // !!! spetsialni sa daljinite 9=3^2 i 27=3^3, za koito protsenta ilyuminatstvo e po-golyam :)
              add[9]=60; add[27]=70;
              // smyata se sredniya procent ilyuminatstvo ot prochetenite saobshteniya i se dobavya protsenta spriyamo imeto
              percentage=add[name.length()]+(sb/br);
              System.out.println(name+" eliomenat?! POTVARDENO!11!! (na "+percentage+"%).");
              }
       public static void main (String []args) throws Exception {
              Scanner input = new Scanner (System.in);
              int person,key,num,limit;
              String keyword,name,nickname,ID;
              // za nai-golyamo udobshtvo ima komandi za podkana i menyu za izbor za obrabotka
              for (;;) {
                  System.out.println("Menyu: 1 -> Analizirane logovete na Barak Obama.");
                  System.out.println("       2 -> Analizirane logovete na Angela Merkel.");
                  System.out.println("       3 -> Analizirane logovete na Djordjano.");
                  System.out.println("       4 -> Analizirane logovete na drug.");
                  System.out.println("       drugo -> Prekusvane na programata.");
                  System.out.println("Molya vavedete cialo chislo i sled tova natisnete Enter.");
                  person=input.nextInt();
                  if ((person!=1)&&(person!=2)&&(person!=3)&&(person!=4)) break;
                  System.out.println("Molya vavedete edno cialo chislo - limita na broia saobshtenia, koito shte badat analizirani, ili 0 ako iskate vsichkite, i sled tova natisnete Enter");
                  limit=input.nextInt();
                  System.out.println("Molya vavedete edno cialo chislo - klyucha za kriptirane i sled tova natisnete Enter.");
                  key=input.nextInt();
                  System.out.println("Molya vavedete edno cialo chislo - daljinata na dumite, schitani za ilyumenatski, i sled tova natisnete Enter.");
                  num=input.nextInt();
                  System.out.println("Molya vavedete klyuchovata duma i sled tova natisnete Enter.");
                  keyword=input.nextLine();
                  keyword=input.nextLine(); len=-1;
                  //System.out.println(keyword);
                  if (person==1) work("Obama","Bara4e7061","5393259",key,num,keyword,limit);
                  else if (person==2) work("Angela Merkel","Merkel4e54","12904",key,num,keyword,limit);
                  else if (person==3) work("Djordjano","DzhordzhanoBulgaria","19",key,num,keyword,limit);
                  else { System.out.println("Molya vavedete imeto na choveka, na kogoto shte se analizirat logovete, i sled tova natisnete Enter.");
                         name=input.nextLine();
                         System.out.println("Molya vavedete nickname-a na choveka, na kogoto shte se analizirat logovete, i sled tova natisnete Enter.");
                         nickname=input.nextLine();
                         System.out.println("Molya vavedete ID-to na choveka, na kogoto shte se analizirat logovete, i sled tova natisnete Enter.");
                         ID=input.nextLine();
                         work(name,nickname,ID,key,num,keyword,limit); }
                  }
       System.out.println("Programata zavarshi izpalnenieto si.");
       }
}
