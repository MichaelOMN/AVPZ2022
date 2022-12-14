package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class REST {
    public static final String MAIN_URL = "http://michael0832.pythonanywhere.com/";
    public static final String REGISTER_ENDPOINT = "register";
    public static final String LOGIN_ENDPOINT = "login";
    public static final String USERS_ENDPOINT = "users";
    public static final String USER_ENDPOINT = "user";
    public static final String ACCOUNT_ENDPOINT = "account";
    public static final String ADVS_ENDPOINT = "advs";
    public static final String ADV_ENDPOINT = "adv";
    public static final String REMOTERS_ENDPOINT = "remoters";
    public static final String PIC_ENDPOINT = "pic";
    public static final String ALL_ADS = "advs_available";
    public static final String COMPLAINT = "complaint";
    public static final String COMMENT = "comment";
    public static final String COMMENTS = "comments";
    public static final String FAVOURITES = "favourites";
    private static final String FAVOURITE = "favourite";


    private static String post(String endpoint, String body) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MAIN_URL + endpoint))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }


    private static String get(String endpoint) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MAIN_URL + endpoint))
                .headers("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private static String put(String endpoint, String body) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MAIN_URL + endpoint))
                .headers("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String register(String text) {
        try {
            String response = post(REGISTER_ENDPOINT, text);
            return response;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static String login(String text) {
        try {
            String response = post(LOGIN_ENDPOINT, text);
            return response;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static String users() {
        try {
            String response = get(USERS_ENDPOINT);
            return response;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static String account(String token, String body) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MAIN_URL + ACCOUNT_ENDPOINT))
                .headers("Content-Type", "application/json", "x-access-tokens", token)
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String advs(String token) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MAIN_URL + ADVS_ENDPOINT))
                .headers("Content-Type", "application/json", "x-access-tokens", token)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String saveAdv(String token, String body) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MAIN_URL + ADV_ENDPOINT))
                .headers("Content-Type", "application/json", "x-access-tokens", token)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String editAdv(String token, String body, int id) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MAIN_URL + ADV_ENDPOINT + "/" + id))
                .headers("Content-Type", "application/json", "x-access-tokens", token)
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String deleteAdv(String token, int id) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MAIN_URL + ADV_ENDPOINT + "/" + id))
                .headers("Content-Type", "application/json", "x-access-tokens", token)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String remoters() {
        try {
            String response = get(REMOTERS_ENDPOINT);
            return response;
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    public static String sendPicOfProfile(String token, String path) throws Exception {
      /*  HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MAIN_URL + "account" + "/" + PIC_ENDPOINT))
                .headers("Content-Type", "image/jpeg", "x-access-tokens", token)
                .POST(HttpRequest.BodyPublishers.ofFile(file))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();*/
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("profile_image", path,
                        RequestBody.create(MediaType.parse("image/png"),
                                new File(path)))
                .build();
        Request request = new Request.Builder()
                .url("http://michael0832.pythonanywhere.com/account/pic")
                .method("POST", body)
                .addHeader("x-access-tokens", token)
                .build();
        Response response = client.newCall(request).execute();

        return Objects.requireNonNull(response.body()).string();
    }

    public static String sendPicOfAd(String token, String path, int adId) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("advertisement_image", path,
                        RequestBody.create(MediaType.parse("image/png"),
                                new File(path)))
                .build();
        Request request = new Request.Builder()
                .url("http://michael0832.pythonanywhere.com/adv/" + adId + "/pic")
                .method("POST", body)
                .addHeader("x-access-tokens", token)
                .build();
        Response response = client.newCall(request).execute();

        return Objects.requireNonNull(response.body()).string();
    }

    public static String getUserInfo(String token) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MAIN_URL + USER_ENDPOINT))
                .headers("Content-Type", "application/json", "x-access-tokens", token)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String getImage(String token, String id) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MAIN_URL + PIC_ENDPOINT + "/" + id))
                .headers("Content-Type", "application/json", "x-access-tokens", token)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String getAllAds() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MAIN_URL + ALL_ADS))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }


    public static String getComplaintsForAd(String id) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(MAIN_URL + ADV_ENDPOINT + "/" + id + "/" + "complaints"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String addComplaint(String token, String body) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MAIN_URL + COMPLAINT))
                .headers("Content-Type", "application/json", "x-access-tokens", token)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }


    public static String addComment(String token, String body) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MAIN_URL + COMMENT))
                .headers("Content-Type", "application/json", "x-access-tokens", token)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String getCommentsOnAd(String id) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MAIN_URL + COMMENTS+ "/" + id))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }


    public static String deleteComment(String token, int id) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MAIN_URL + COMMENT + "/" + id))
                .headers("Content-Type", "application/json", "x-access-tokens", token)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String getFavourites(String token) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MAIN_URL + FAVOURITES))
                .headers("Content-Type", "application/json", "x-access-tokens", token)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }


    public static String addFavourite(String token, String body) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MAIN_URL + FAVOURITE))
                .headers("Content-Type", "application/json", "x-access-tokens", token)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String deleteFavourite(String token, int id) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MAIN_URL + FAVOURITE + "/" + id))
                .headers("Content-Type", "application/json", "x-access-tokens", token)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }


    public static String getAdById(String id) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MAIN_URL + ADVS_ENDPOINT+ "/" + id))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static void main(String[] args) throws Exception {
        //System.out.println(users());


        // System.out.println(register(Reader.readFile("register.txt")));
       /* String token = login(Util.readFile("login.txt"));

        JsonObject jsonObject = new Gson().fromJson(token, JsonObject.class);
        String tkn = String.valueOf(jsonObject.get("token"));
        tkn = tkn.substring(1, tkn.length() - 1);

        //System.out.println(account(tkn, server.Reader.readFile("account.txt")));
        System.out.println(tkn);
        System.out.println(remoters());*/
        /*String jsonList = Util.getValueByKeyJSON("List of your ads");*/

        //System.out.println(saveAdv(tkn, Util.readFile("adv.txt")));
        //System.out.println(editAdv(tkn, Reader.readFile("adv.txt"), 1));
        //System.out.println(deleteAdv(tkn, 2));
        //System.out.println(remoters());
        //getPicById("54cef319-3178-4fe9-8a3b-ad19b2ff7501", "img.jpeg");;
        //System.out.println(sendPic(tkn, Paths.get("D:\\Java_Project\\AVPZ\\src\\main\\resources\\flat.jpg")));
     /*   String info = getUserInfo(tkn);
        String picId = Util.getValueByKeyJSON(info, "photo_file");
        String pic = getImage(tkn, picId);
       Image image = new Image(new ByteArrayInputStream(pic.getBytes(StandardCharsets.UTF_8)));
        System.out.println(image.getRequestedWidth());*/
        //System.out.println(sendPic(tkn, "D:\\Java_Project\\AVPZ\\src\\main\\resources\\images2.png"));




    }
}
