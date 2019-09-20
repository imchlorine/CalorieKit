package com.maclee.calorie.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Source code from FatSecret
 */
public class FatSecretConnection {
    private final String APP_KEY;
    private final String APP_SECRET;
    private final String APP_URL = "http://platform.fatsecret.com/rest/server.api";
    private final String APP_SIGNATURE_METHOD = "HmacSHA1";
    private final String HTTP_METHOD = "GET";

    public FatSecretConnection(String APP_KEY, String APP_SECRET) {
        this.APP_KEY = APP_KEY;
        this.APP_SECRET = APP_SECRET;
    }

    public String nonce() {
        Random r = new Random();
        StringBuffer n = new StringBuffer();

        for(int i = 0; i < r.nextInt(8) + 2; ++i) {
            n.append(r.nextInt(26) + 97);
        }

        return n.toString();
    }

    public String[] generateOauthParams() {
        String[] a = new String[]{"oauth_consumer_key=" + this.APP_KEY, "oauth_signature_method=HMAC-SHA1", "oauth_timestamp=" + (new Long(System.currentTimeMillis() / 1000L)).toString(), "oauth_nonce=" + this.nonce(), "oauth_version=1.0", "format=json"};
        return a;
    }

    public String join(String[] params, String separator) {
        StringBuffer b = new StringBuffer();

        for(int i = 0; i < params.length; ++i) {
            if (i > 0) {
                b.append(separator);
            }

            b.append(params[i]);
        }

        return b.toString();
    }

    public String paramify(String[] params) {
        String[] p = (String[]) Arrays.copyOf(params, params.length);
        Arrays.sort(p);
        return this.join(p, "&");
    }

    public String encode(String url) {
        if (url == null) {
            return "";
        } else {
            try {
                return URLEncoder.encode(url, "utf-8").replace("+", "%20").replace("!", "%21").replace("*", "%2A").replace("\\", "%27").replace("(", "%28").replace(")", "%29");
            } catch (UnsupportedEncodingException var3) {
                throw new RuntimeException(var3.getMessage(), var3);
            }
        }
    }

    public String sign(String method, String uri, String[] params) throws UnsupportedEncodingException {
        String encodedURI = this.encode(uri);
        String encodedParams = this.encode(this.paramify(params));
        String[] p = new String[]{method, encodedURI, encodedParams};
        String text = this.join(p, "&");
        String key = this.APP_SECRET + "&";
        SecretKey sk = new SecretKeySpec(key.getBytes(), "HmacSHA1");
        String sign = "";

        try {
            Mac m = Mac.getInstance("HmacSHA1");
            m.init(sk);
            sign = this.encode((new String(Base64.encode(m.doFinal(text.getBytes()), 0))).trim());
        } catch (NoSuchAlgorithmException var12) {
            System.out.println("NoSuchAlgorithmException: " + var12.getMessage());
        } catch (InvalidKeyException var13) {
            System.out.println("InvalidKeyException: " + var13.getMessage());
        }

        return sign;
    }

    public String buildFoodsSearchUrl(String query, int pageNumber) throws Exception {
        List<String> params = new ArrayList(Arrays.asList(this.generateOauthParams()));
        String[] template = new String[1];
        params.add("method=foods.search");
        params.add("max_results=50");
        params.add("page_number=" + pageNumber);
        params.add("search_expression=" + this.encode(query));
        params.add("oauth_signature=" + this.sign("GET", "http://platform.fatsecret.com/rest/server.api", (String[])params.toArray(template)));
        return "http://platform.fatsecret.com/rest/server.api?" + this.paramify((String[])params.toArray(template));
    }

    public String buildFoodGetUrl(Long id) throws Exception {
        List<String> params = new ArrayList(Arrays.asList(this.generateOauthParams()));
        String[] template = new String[1];
        params.add("method=food.get");
        params.add("food_id=" + id);
        params.add("oauth_signature=" + this.sign("GET", "http://platform.fatsecret.com/rest/server.api", (String[])params.toArray(template)));
        return "http://platform.fatsecret.com/rest/server.api?" + this.paramify((String[])params.toArray(template));
    }

    public String buildRecipesSearchUrl(String query, int pageNumber) throws Exception {
        List<String> params = new ArrayList(Arrays.asList(this.generateOauthParams()));
        String[] template = new String[1];
        params.add("method=recipes.search");
        params.add("max_results=50");
        params.add("page_number=" + pageNumber);
        params.add("search_expression=" + this.encode(query));
        params.add("oauth_signature=" + this.sign("GET", "http://platform.fatsecret.com/rest/server.api", (String[])params.toArray(template)));
        return "http://platform.fatsecret.com/rest/server.api?" + this.paramify((String[])params.toArray(template));
    }

    public String buildRecipeGetUrl(Long id) throws Exception {
        List<String> params = new ArrayList(Arrays.asList(this.generateOauthParams()));
        String[] template = new String[1];
        params.add("method=recipe.get");
        params.add("recipe_id=" + id);
        params.add("oauth_signature=" + this.sign("GET", "http://platform.fatsecret.com/rest/server.api", (String[])params.toArray(template)));
        return "http://platform.fatsecret.com/rest/server.api?" + this.paramify((String[])params.toArray(template));
    }
}
