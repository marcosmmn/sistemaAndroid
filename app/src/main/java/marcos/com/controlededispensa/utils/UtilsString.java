package marcos.com.controlededispensa.utils;

public class UtilsString {
    public static boolean stringVazia(String texto){

        return texto == null || texto.trim().length() == 0;
    }
}
