import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void sortImg(String[] t, String[] arrType, String format) {
    System.out.println();
    for(int i = 0; i < t.length; i++) {
      if(t[i].startsWith("\"http") && t[i].endsWith(format + "\",")) {
        arrType[i] = t[i];  
        if(arrType[i].charAt(9) == 'i') {
          System.out.println("Image (" + format + "): " + arrType[i]);            
        }
      }
    } 
  }
  public static void sortPunctiation(String[] t, String end1, String end2, String end3) {
      for(int i = 0; i < t.length; i++) {
         if(t[i].endsWith(end1) || t[i].endsWith(end2) || t[i].endsWith(end3)) {
           System.out.println("\n" + t[i].replaceAll("\"", " "));
         }
       }
  }

  // function for making requests.
  public static HttpResponse<String> makeRequest(String url) throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();

    HttpRequest rec = HttpRequest.newBuilder()
      .GET()
      .header("accept", "application/json")
      .uri(URI.create(url))
      .build();

    HttpResponse<String> res = client.send(rec, HttpResponse.BodyHandlers.ofString());

    return res;
  }
  public static void main(String[] args) throws IOException, InterruptedException {
    Scanner input = new Scanner(System.in);
    
    System.out.println("\n--- options ---" + "\n0) Tell me a bad joke" + "\n1) Get images from reddit" + "\n2) Get dog image\n");
    System.out.print("Select: ");
    int option = input.nextInt();

    HttpResponse<String> res = makeRequest("https://httpbin.org/200");

    // make request and store result based on user input.
    if(option == 0) {
      res = makeRequest("https://v2.jokeapi.dev/joke/Programming");
    } else if(option == 1) {
      System.out.print("Subreddit: ");
      String sub = input.nextLine();


      // Uses if statement because sub can be read as an empty string.
      if(!sub.equals("")) {
        res = makeRequest("https://www.reddit.com/r/" + sub + "/.json?nsfw=0");
      } else {
        sub = input.nextLine();
        res = makeRequest("https://www.reddit.com/r/" + sub + "/.json?nsfw=0");
      }
    } else if(option == 2) {
      res = makeRequest("https://random.dog/woof.json");
    }
    if(option < 4) {
      // Remove whitespace from response data for easy formating.
      String resFormat = res.body().replaceAll("\\s+"," ");

      if(res.statusCode() < 300) {
        
        String[] t = resFormat.split(" ", 0), 
        t2 = resFormat.split("\",", 0),
        t3 = resFormat.split(",");
        
        String[] images = new String[t.length];

        if(option < 1) {
          sortPunctiation(t2, ".", "?", "!");          
        } else if(option == 1) {
          System.out.println("\n-------------------------------- png --------------------------------");
          sortImg(t, images, "png");
          System.out.println("\n-------------------------------- jpg --------------------------------");
          sortImg(t, images, "jpg");          
        } else if(option == 2) {
          System.out.println("\n" + t3[1].replaceAll("\"", " "));
        }
      } else {
        System.out.println(res.statusCode());
      }
    }
  }
}