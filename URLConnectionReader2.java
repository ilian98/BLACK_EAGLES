import java.net.*;
import java.io.*;
import java.util.*;

public class URLConnectionReader {
       static String []voc=new String[300001];
       static String[] words=new String[4000001];
       static int len;
       public static String getText(String url) throws Exception {
              // reads text from some url
              URL website = new URL(url);
              URLConnection connection = website.openConnection();
              BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
              StringBuilder response = new StringBuilder();
              String inputLine;
              while ((inputLine=in.readLine())!=null) {
                    // if len is -1 then the current url is the one with the logs
                    // otherwise the current url is the one with the words from the dictionary
                    if (len==-1) response.append(inputLine);
                    else { // words with capital first letter or one-letter words (except "a" or "i") aren't recorded
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
              // for no future problems each word is made to have only small letters
              word=word.toLowerCase();
              // it is made a binary search in the ordered array with words
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
       public static boolean check (int beg, int end, String key1) {
              // it is checked how many words in the interval aren't existing in the vocabulary
              int h,br=0;
              for (h=beg; h<=end; h++) {
                  // a check if it is a word
                  if ((words[h].charAt(0)>='0')&&(words[h].charAt(0)<='9')) continue;
                  // a check if it is the key word
                  if (words[h].compareTo(key1)==0) continue;
                  // a check if it's not a name
                  if ((words[h].charAt(0)>='A')&&(words[h].charAt(0)<='Z')) continue;
                  // a check if the word is existing
                  if (search(words[h])==false) { 
                     // if it's not existing, it is checked if it ends with -s or -ing, or -ed
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
                     // if even after these checks the word is not existing then we increase their number in fl
                     br++;
                     // if the number of the non-existing words is greater than for it means that the message is encrypted
                     if (br>4) return true;
                     }
                  }
              return false;
              }
       public static void work (String name, String nickname, String ID, int next, int num, String key1, int limit) throws Exception {
              char cur,prev1;
              // i, j and h are variables for the "for" loops
              int i,j,h,fl1,ind=-1,val,sb=0,br=0,last1=-1,last2=-1,curind,st,percentage;
              boolean fl;
              int []avg = new int[4000001];
              int []inds = new int[4000001];
              int []add = new int[31];
              boolean []key = new boolean[4000001];
              Set <String> accounts = new HashSet<String>();
              String key2="three",key3="",key4="",ex="",encrypted="";
              // in key3 and key4 are recorded what the words "three" and the key word would be after their encryption
              // it is created the file with the IBAN's of bank cards which would be found in the logs
              PrintWriter ibans = new PrintWriter (name+"_discussion_"+"iban_of_bank_accounts.txt","UTF-8");
              String content;
              // the text from the url is being read according the parameters for the man and the limit of the messages
              if (limit==0) content=URLConnectionReader.getText("http://facebook-com-secret-nsa-chat-api-that-noone-knows-about.devlabs-projects.com/user/"+ID+"/chats/?API_KEY=BLACK_EAGLES_666");
              else content=URLConnectionReader.getText("http://facebook-com-secret-nsa-chat-api-that-noone-knows-about.devlabs-projects.com/user/"+ID+"/chats/?API_KEY=BLACK_EAGLES_666&limit="+limit); 
              for (i=0; i<key1.length(); i++) {
                  if ((key1.charAt(i)>='a')&&(key1.charAt(i)<='z')) key3+=((char)(((key1.charAt(i)-'a'+next)%26)+'a'));
                  else if ((key1.charAt(i)>='A')&&(key1.charAt(i)<='Z')) key3+=((char)((key1.charAt(i)-'A'+next)%26)+'A');
                  else key3+=key1.charAt(i);
                  }
              for (i=0; i<key2.length(); i++) {
                  if ((key2.charAt(i)>='a')&&(key2.charAt(i)<='z')) key4+=((char)(((key2.charAt(i)-'a'+next)%26)+'a'));
                  else if ((key2.charAt(i)>='A')&&(key2.charAt(i)<='Z')) key4+=((char)(((key2.charAt(i)-'A'+next)%26)+'A'));
                  else key4+=key2.charAt(i);
                  }
              // a check if the url has the intended information
              if (content.charAt(11)!='s') {
                 System.out.println("Wrong Url!");
                 return ;
                 }
              // the text is being split into words for easier processing
              for (i=1; i<content.length(); i++) {
                  prev1=content.charAt(i-1);
                  cur=content.charAt(i);
                  if ((cur!='{')&&(cur!='\"')&&(cur!=',')&&(cur!='.')&&
                      (cur!=' ')&&(cur!='}')&&(cur!='[')&&(cur!=']')&&
                      (cur!=':')&&(cur!=';')&&(cur!='?')&&(cur!='!')&&
                      (cur!='\\')) {
                     if ((prev1=='\"')||(prev1==' ')) {
                        // when i viewed the logs i noticed that there are some fragments of this type: \ u 0 0 0 ... (without the spaces) which we don't count for words
                        // they are unicode characters like o (degree) or """ and for that reason are removed
                        if ((ind!=-1)&&(words[ind].length()==5)&&(words[ind].charAt(0)=='u')&&(words[ind].charAt(1)=='0')) ind--;
                        ind++; words[ind]="";
                        }
                     words[ind]+=cur;
                     }
                  }
              ind++;
              // further processing of the words
              for (i=0; i<ind; i++) {
                  ex="";
                  for (j=0; j<words[i].length(); j++) {
                      // there are words that have "-" whick are made into the first part (the first word) only for easier further processing
                      if (words[i].charAt(j)=='-') {
                         if (ex.length()==0) continue;
                         break;
                         }
                      // if there are merged braces with the words the braces are removed
                      if ((words[i].charAt(j)=='(')||(words[i].charAt(j)==')')) continue;
                      if (j==words[i].length()-1) {
                         ex+=words[i].charAt(j);
                         continue;
                         }
                      // there are words where the apostrophe is recorded as \u00085 and this is removed from the word
                      if ((words[i].charAt(j)=='u')&&(words[i].charAt(j+1)=='0')) {
                         if ((words[i].charAt(j+3)=='8')&&(words[i].charAt(j+4)=='5')) break;
                         j+=4;
                         continue;
                         }
                      ex+=words[i].charAt(j);
                      }
                  words[i]=ex;
                  }
              len=0;
              // recording the vocabulary
              URLConnectionReader.getText("https://raw.githubusercontent.com/dwyl/english-words/master/words2.txt");
              // analyzing for bank cards
              for (i=0; i<ind; i++) {
                  // a check if the length of the word is suitable for such of an IBAN
                  if ((words[i].length()>=16)&&(words[i].length()<=31)) {
                     // a check if the first four signs match the IBAN pattern
                     if ((words[i].charAt(0)>='A')&&(words[i].charAt(0)<='Z')&&
                         (words[i].charAt(1)>='A')&&(words[i].charAt(1)<='Z')&&
                         (words[i].charAt(2)>='0')&&(words[i].charAt(2)<='9')&&
                         (words[i].charAt(3)>='0')&&(words[i].charAt(3)<='9')) {
                        // check if the IBAN is in an encrypted message
                        if (check(Math.max(0,i-5),Math.min(ind-1,i+5),key1)==true) {
                           encrypted="";
                           for (h=0; h<words[i].length(); h++) {
                               if ((words[i].charAt(h)>='A')&&(words[i].charAt(h)<='Z')) {
                                  encrypted+=((char)((words[i].charAt(h)-'A'+26-next)%26+'A'));
                                  }
                               else if ((words[i].charAt(h)>='a')&&(words[i].charAt(h)<='z')) {
                                       encrypted+=((char)((words[i].charAt(h)-'a'+26-next)%26+'a'));
                                       }
                               else if ((words[i].charAt(h)>='0')&&(words[i].charAt(h)<='9')) {
                                       encrypted+=words[i].charAt(h);
                                       }
                               else break;
                               }
                           words[i]=encrypted;
                           }
                        // standard modulo 97 check if the IBAN of the bank card is valid
                        fl1=0; val=0;
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
                            else { fl1++; break; }
                            }
                        if (fl1==0) {
                           val*=100; val+=words[i].charAt(0)-'A'+10;
                           val%=97;
                           val*=100; val+=words[i].charAt(1)-'A'+10;
                           val%=97;
                           val*=10; val+=words[i].charAt(2)-'0';
                           val%=97;
                           val*=10; val+=words[i].charAt(3)-'0';
                           val%=97;
                           if (val==1) {
                              // if this really is a valid IBAN card and until now it is not SAVED IN THE FILE - it is SAVED IN THE FILE and in the set
                              //System.out.println(check(i-1,i-1,key1));
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
              for (i=ind-1; i>2; i--) {
                  // analyzing the logs backwards for fast processing if there is the key word in a message
                  for (j=i; j>2; j--) {
                      if ((words[j].compareTo("author")==0)&&(words[j+2].compareTo("message")==0)) {
                         // every message is from the word after the word "author, the word with the name of the author and the word "message"
                         // in fl it is recorded if the message is encrypted or not
                         fl=check(j+3,i,key1);
                         key[j]=false;
                         // a check if the key word is in the text, counting the words with the sought length, the digit or the word "three" taking into account if the message is encrypted
                         for (h=j+3; h<=i; h++) {
                             if (fl==true) {
                                if (words[h].compareTo(key3)==0) key[j]=true;
                                if (words[h].compareTo(key4)==0) avg[j]++;
                                else if (words[h].length()==num) avg[j]++;
                                else if (words[h].compareTo("3")==0) avg[j]++;
                                }
                             else { if (words[h].compareTo(key1)==0) key[j]=true;
                                    if (words[h].compareTo(key2)==0) avg[j]++;
                                    else if (words[h].length()==num) avg[j]++;
                                    else if (words[h].compareTo("3")==0) avg[j]++; }
                             }
                         // in sb it is recorded the current percentage illuminati
                         avg[j]=avg[j]*100/(i-j-2); sb+=avg[j];
                         // a check if the current message is from the man for whom the logs are analyzed
                         if (words[j+1].compareTo(nickname)==0) {
                            // in last2 it is recorded that the last found message for the current person is the current message
                            last2=j;
                            // if there is no message of other person after the current it is terminated the current processing
                            if (last1==-1) inds[j]=-1;
                            else { if (key[j]==true) {
                                      // the key word is in the current message
                                      // it is recorded the index in the array inds
                                      inds[j]=last1;
                                      // curing in the cycle is the index of the current person
                                      curind=last1;
                                      // the power is the "weight" with which every next message would be taken
                                      st=2;
                                      // there is no sense for h to be more than 6 because the "weight" would be too big and the increment won't change the value
                                      for (h=1; h<=6; h++) {
                                          // it is calculated the percentage of illuminati according the "weight"
                                          sb+=avg[curind]/st;
                                          // the "weight" is increasing
                                          st*=2;
                                          // if there isn't the key word anymore it is terminated
                                          if (key[curind]==false) break;
                                          // otherwise it is passed to the first nearest message of other person
                                          curind=inds[curind];
                                          // if such doesn't exist it is terminated
                                          if (curind==-1) break;
                                          }
                                      } }
                            }
                         else { // if it's not for that person in last1 it is recorded the last found message which is not from the current person
                                last1=j;
                                // and in inds[j] it is recorded the value of last2, which is used for a link to the first nearest message after the current, which is from the man for whom it is processing
                                inds[j]=last2; }
                        // it is incremented br which is the denominator in the calculation of the average in the end
                         br++;
                         i=j; break;
                         }
                      }
                  }
              // if accidentally there aren't messages of the current person then to prevent the deletion to 0 br is incremented to become 1
              if (br==0) br++;
              // in the array aa for every length of the name of the person it is recorded the percentage illuminati which would be added
              // for each number which is divisible by 3 it is recorded 50, and for the others - 30
              for (i=3; i<=30; i++) {
                  add[i]=50; add[i-1]=add[i-2]=30;
                  }
              // !!! special cases for the lengths are 9=3*3 and 27=3^3 for which the percentage is bigger :)
              add[9]=60; add[27]=70;
              // it is calculated the average percentage illuminati from the processed messages and it is added the percentage according the name
              percentage=add[name.length()]+(sb/br);
              System.out.println(name+" eliomenat?! POTVARDENO!11!! (na "+percentage+"%).");
              }
       public static void main (String []args) throws Exception {
              Scanner input = new Scanner (System.in);
              int person,key,num,limit;
              String keyword,name,nickname,ID;
              // for greater convinience to Ginka there are commands which say what has to be entered
              for (;;) {
                  System.out.println("Menyu: 1 -> Analizirane logovete na Barak Obama.");
                  System.out.println("       2 -> Analizirane logovete na Angela Merkel.");
                  System.out.println("       3 -> Analizirane logovete na Djordjano.");
                  System.out.println("       4 -> Analizirane logovete na drug.");
                  System.out.println("       drugo -> Prekusvane na programata.");
                  System.out.println("Barak Obama e s nickname Bara4e7061 i ID 5393259.");
                  System.out.println("Angela Merkel e s nickname Merkel4e54 i ID 12904.");
                  System.out.println("Djordjano e s nickname DzhordzhanoBulgaria i ID 19.");
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
