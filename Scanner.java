import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {

    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    private int linea = 1;

    private static final Map<String, TipoToken> palabrasReservadas, simbolos, oprel, cadena, numero, identificador, comentarios;

    static{
        simbolos = new HashMap<>();
        simbolos.put("(", TipoToken.IZQ_PAR);
        simbolos.put(")", TipoToken.DER_PAR);
        simbolos.put("{", TipoToken.IZQ_LLAVE);
        simbolos.put("}", TipoToken.DER_LLAVE);
        simbolos.put(",", TipoToken.COMMA);
        simbolos.put(".", TipoToken.DOT);
        simbolos.put(";", TipoToken.SEMICOLON);
        simbolos.put("-", TipoToken.DASH);
        simbolos.put("+", TipoToken.ADD);
        simbolos.put("*", TipoToken.ASTERISK);
        simbolos.put("/", TipoToken.SLASH);

        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("if", TipoToken.IF);
        palabrasReservadas.put("class", TipoToken.CLASS);
        palabrasReservadas.put("else", TipoToken.ELSE);
        palabrasReservadas.put("for", TipoToken.FOR);
        palabrasReservadas.put("print", TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("false", TipoToken.FALSE);
        palabrasReservadas.put("true", TipoToken.TRUE);
        palabrasReservadas.put("while", TipoToken.WHILE);
        palabrasReservadas.put("null", TipoToken.NULL);
        palabrasReservadas.put("and", TipoToken.AND);
        palabrasReservadas.put("fun", TipoToken.FUN);
        palabrasReservadas.put("or", TipoToken.OR);
        palabrasReservadas.put("super", TipoToken.SUPER);
        palabrasReservadas.put("this", TipoToken.THIS);
        palabrasReservadas.put("var", TipoToken.VAR);

        oprel = new HashMap<>();
        oprel.put("<=", TipoToken.LE);
        oprel.put(">=", TipoToken.GE);
        oprel.put(">", TipoToken.LL);
        oprel.put("<", TipoToken.GG);
        oprel.put("<>", TipoToken.NE);
        oprel.put("==", TipoToken.EQ);
        oprel.put("=", TipoToken.EQEQ);
        oprel.put("!", TipoToken.NN);
        oprel.put("!=", TipoToken.NET);

        cadena = new HashMap<>();
        cadena.put("\"", TipoToken.CADENA);

        numero = new HashMap<>();
        numero.put("#", TipoToken.NUMERO);

        comentarios = new HashMap<>();
        comentarios.put("//", TipoToken.COMMENT);
        comentarios.put("/*", TipoToken.COMMENTCOMMENT);

        identificador = new HashMap<>();
        identificador.put("\"\"",TipoToken.IDENTIFICADOR);

        /*
        and--
        palabrasReservadas.put("ademas", ); else--
        palabrasReservadas.put("falso", ); false--
        palabrasReservadas.put("para", );for--
        palabrasReservadas.put("fun", ); //definir funciones--?
        palabrasReservadas.put("si", );if--
        palabrasReservadas.put("nulo", );null--
        palabrasReservadas.put("o", );or--
        palabrasReservadas.put("imprimir", ); print--
        palabrasReservadas.put("retornar", );return--
        palabrasReservadas.put("super", );super--
        palabrasReservadas.put("este", );this--
        palabrasReservadas.put("verdadero", ); true--
        palabrasReservadas.put("var", ); //definir variables var--
        palabrasReservadas.put("mientras", ); while--
        */
    }

    Scanner(String source){
        this.source = source;
    }

    List<Token> scanTokens(){
        //AquÃ­ va el corazÃ³n del scanner.
        /*
        Analizar el texto de entrada para extraer todos los tokens
        y al final agregar el token de fin de archivo
         */
        int i=0, bandera1=0;
        //System.out.println(source.length());

        while(i<source.length()){

            //char c = source.charAt(i);
            //System.out.println("i:" + i);
            //System.out.println("lenght: " + source.length());
            //System.out.println("char: " + source.charAt(i));


            //Operadores relacionales
            if(source.charAt(i) == '<'){
                int temp = i;
                temp++;
                if(temp == source.length()){
                    tokens.add(new Token(TipoToken.LL,"<", null,linea));
                }else{
                    i++;
                    if(source.charAt(i) == '='){
                        tokens.add(new Token(TipoToken.LE,"<=", null,linea));
                        //i++;
                    }else if(source.charAt(i) == '>'){
                        tokens.add(new Token(TipoToken.NE,"<>", null,linea));
                        //i++;
                    }else{
                        tokens.add(new Token(TipoToken.LL,"<", null,linea));
                        i--;
                    }

                }
            }else if(source.charAt(i) == '='){
                int temp = i;
                temp++;
                //System.out.println(i);
                if(temp == source.length()){
                        tokens.add(new Token(TipoToken.EQEQ,"=", null,linea));
                        i++;
                    //System.out.println("asdf");
                }else{
                    i++;
                    if(source.charAt(i) == '='){
                        tokens.add(new Token(TipoToken.EQ,"==", null,linea));
                    }else{
                        tokens.add(new Token(TipoToken.EQEQ,"=", null,linea));
                    }
                    i--;
                }
            }else if(source.charAt(i) == '>'){
                int temp = i;
                temp++;
                if(temp == source.length()){
                    tokens.add(new Token(TipoToken.GG,">", null,linea));
                }else{
                    i++;
                    if(source.charAt(i) == '='){
                        tokens.add(new Token(TipoToken.GE,">=", null,linea));
                        //i++;
                    }else if(source.charAt(i) ==  ' '){
                        tokens.add(new Token(TipoToken.GG,">", null,linea));
                    }
                }
            }else if(source.charAt(i) == '!'){
                int temp = i;
                temp++;
                if(temp == source.length()){
                    tokens.add(new Token(TipoToken.NN,"!", null,linea));
                }else{
                    i++;
                    if(source.charAt(i) == '='){
                        tokens.add(new Token(TipoToken.NET,"!=", null,linea));
                        //i++;
                    }else if(source.charAt(i) ==  ' '){
                        //tokens.add(new Token(TipoToken.NN,"!", null,linea));
                        tokens.add(new Token(oprel.get("!"),"!",null,linea));
                    }
                }
            }else if(source.charAt(i) == '('){// Simbolos
                int temp = i;
                temp++;
                if(temp == source.length()){
                    tokens.add(new Token(simbolos.get("("),"(",null,linea));
                }else{
                    if(source.charAt(i) ==  '('){
                        //tokens.add(new Token(TipoToken.NN,"!", null,linea));
                        tokens.add(new Token(simbolos.get("("),"(",null,linea));
                    }
                    //i++;
                }
            }else if(source.charAt(i) == ')'){
                int temp = i;
                temp++;
                if(temp == source.length()){
                    tokens.add(new Token(simbolos.get(")"),")",null,linea));
                }else{
                    if(source.charAt(i) ==  ')'){
                        //tokens.add(new Token(TipoToken.NN,"!", null,linea));
                        tokens.add(new Token(simbolos.get(")"),")",null,linea));
                    }
                    //i++;
                }
            }else if(source.charAt(i) == '{'){
                int temp = i;
                temp++;
                if(temp == source.length()){
                    tokens.add(new Token(simbolos.get("{"),"{",null,linea));
                }else{
                    if(source.charAt(i) ==  '{'){
                        //tokens.add(new Token(TipoToken.NN,"!", null,linea));
                        tokens.add(new Token(simbolos.get("{"),"{",null,linea));
                    }
                    //i++;
                }
            }else if(source.charAt(i) == '}'){
                int temp = i;
                temp++;
                if(temp == source.length()){
                    tokens.add(new Token(simbolos.get("}"),"}",null,linea));
                    i++;
                }else{
                    if(source.charAt(i) ==  '}'){
                        //tokens.add(new Token(TipoToken.NN,"!", null,linea));
                        tokens.add(new Token(simbolos.get("}"),"}",null,linea));
                    }
                    //i++;
                }
            }else if(source.charAt(i) == ','){
                int temp = i;
                temp++;
                if(temp == source.length()){
                    tokens.add(new Token(simbolos.get(","),",",null,linea));
                }else{
                    if(source.charAt(i) ==  ','){
                        //tokens.add(new Token(TipoToken.NN,"!", null,linea));
                        tokens.add(new Token(simbolos.get(","),",",null,linea));
                    }
                    //i++;
                }
            }else if(source.charAt(i) == '.'){
                int temp = i;
                temp++;
                if(temp == source.length()){
                    tokens.add(new Token(simbolos.get("."),".",null,linea));
                }else{
                    if(source.charAt(i) ==  '.'){
                        //tokens.add(new Token(TipoToken.NN,"!", null,linea));
                        tokens.add(new Token(simbolos.get("."),".",null,linea));
                    }
                    //i++;
                }
            }else if(source.charAt(i) == ';'){
                int temp = i;
                temp++;
                if(temp == source.length()){
                    tokens.add(new Token(simbolos.get(";"),";",null,linea));
                    i++;
                }else{
                    if(source.charAt(i) ==  ';'){
                        //tokens.add(new Token(TipoToken.NN,"!", null,linea));
                        tokens.add(new Token(simbolos.get(";"),";",null,linea));
                    }
                    //i++;
                }
            }else if(source.charAt(i) == '-'){
                int temp = i;
                temp++;
                if(temp == source.length()){
                    tokens.add(new Token(simbolos.get("-"),"-",null,linea));
                }else{
                    if(source.charAt(i) ==  '-'){
                        //tokens.add(new Token(TipoToken.NN,"!", null,linea));
                        tokens.add(new Token(simbolos.get("-"),"-",null,linea));
                    }
                    //i++;
                }
            }else if(source.charAt(i) == '+'){
                int temp = i;
                temp++;
                if(temp == source.length()){
                    tokens.add(new Token(simbolos.get("+"),"+",null,linea));
                }else{
                    if(source.charAt(i) ==  '+'){
                        //tokens.add(new Token(TipoToken.NN,"!", null,linea));
                        tokens.add(new Token(simbolos.get("+"),"+",null,linea));
                    }
                    //i++;
                }
            }else if(source.charAt(i) == '*'){
                int temp = i;
                temp++;
                if(temp == source.length()){
                    tokens.add(new Token(simbolos.get("*"),"*",null,linea));
                }else{
                    if(source.charAt(i) ==  '*'){
                        //tokens.add(new Token(TipoToken.NN,"!", null,linea));
                        tokens.add(new Token(simbolos.get("*"),"*",null,linea));
                    }
                    //i++;
                }
            }else if(source.charAt(i) == '/'){//Comentarios

                int temp = i;
                temp++;
                //System.out.println("temp11: " + temp);
                //System.out.println("temp: " + source.length());
                if(temp == source.length()){
                    tokens.add(new Token(simbolos.get("/"),"/",null,linea));
                    break;
                }else{
                    i++;
                    int temp1 = i;
                    temp1++;
                    //System.out.println("temp121: " + source.charAt(temp1));
                    if(source.charAt(i) == '/'){
                        //tokens.add(new Token(comentarios.get("//"),"//",null,linea));
                        //System.out.println("asdf");
                        i = source.length();
                        break;
                    }else if(source.charAt(i) == '*') {
                        i++;
                        while(true){
                            if(source.charAt(i) == '*' && source.charAt(i+1) == '/') {
                                //System.out.println("Hola: " + i);
                                //tokens.add(new Token(comentarios.get("/*"),"/*",null,linea));
                                i++;
                                break;
                            }else {
                                i++;
                            }
                        }
                    }else{
                        //System.out.println("asdfasdfasdf");
                        tokens.add(new Token(simbolos.get("/"),"/",null,linea));
                        i--;
                        //break;
                    }
                }
            }else if(source.charAt(i) == '"'){//Cadena
                i++;
                String var1 = "", var3 = "";
                StringBuilder sb = new StringBuilder();
                StringBuilder sb1 = new StringBuilder();
                sb.append(var1).append("\"");

                while(true){
                    if((source.charAt(i) >= 'a' && source.charAt(i) <= 'z') || (source.charAt(i) >= 'A' && source.charAt(i) <= 'Z' || source.charAt(i) == ' ' )){
                        sb.append(var1).append(source.charAt(i));
                        sb1.append(var3).append(source.charAt(i));
                        //System.out.println(sb);
                        i++;
                    }else{
                        break;
                    }
                }

                sb.append(var1).append("\"");
                String var2 = sb.toString(), var4 = sb1.toString();

                tokens.add(new Token(cadena.get("\""),var2,sb1,linea));
                //System.out.println(var1);
            }else if(source.charAt(i) >= '0' && source.charAt(i) <= '9'){//Numero
                //i++;
                String var1 = "";
                StringBuilder sb2 = new StringBuilder();
                //StringBuilder sb1 = new StringBuilder();
                //sb.append(var1).append("\"");

                int bandera = 0;

                while(true){
                    //System.out.println(source);
                    //System.out.println("I: "+i);
                    //System.out.println("num:"+source.charAt(i));
                    int temp = i;
                    temp++;

                    if(temp == source.length()){
                        if(source.charAt(i) >= '0' && source.charAt(i) <= '9'){
                            sb2.append(var1).append(source.charAt(i));
                            //sb1.append(var3).append(source.charAt(i));
                            //System.out.println(sb);
                            //i++;
                            //System.out.println("asdf");
                            break;
                        }else {
                            //System.out.println("asdf1");
                            i--;
                            break;
                        }
                    }else{
                        if(source.charAt(i) >= '0' && source.charAt(i) <= '9'){
                            sb2.append(var1).append(source.charAt(i));
                            //sb1.append(var3).append(source.charAt(i));
                            //System.out.println(sb);
                            i++;
                            //System.out.println("asdf2");
                        }else if(source.charAt(i) == '.'){
                            //System.out.println("asdf3");
                            if(bandera == 1){
                                //System.out.println("asdf3");
                                i--;
                                break;
                            }else{
                                //System.out.println("asdf4");
                                sb2.append(var1).append(source.charAt(i));
                                bandera = 1;
                                i++;
                            }
                        }else{
                            //System.out.println("aqeqwe");
                            i--;
                            break;
                        }
                    }
                }

                //sb.append(var1).append("\"");
                String var2 = sb2.toString();

                float result = Float.parseFloat(var2);

                tokens.add(new Token(numero.get("#"),var2,result,linea));
                //System.out.println(var1);
            }else if((source.charAt(i) >= 'a' && source.charAt(i) <= 'z') || (source.charAt(i) >= 'A' && source.charAt(i) <= 'Z')){//Palabras reservadas o Identificadores
                String var1 = "";
                StringBuilder sb = new StringBuilder();
                //StringBuilder sb1 = new StringBuilder();
                //sb.append(var1).append("\"");

                while(true){
                    //System.out.println("num:"+source.charAt(i));
                    //System.out.println("i: "+ i);
                    int temp = i;
                    temp++;
                    if(temp == source.length()){//Revisar que no se pase del final del arreglo, despues de revisar la ultima letra
                        sb.append(var1).append(source.charAt(i));
                        //i++;
                        break;
                    }else if((source.charAt(i) >= 'a' && source.charAt(i) <= 'z') || (source.charAt(i) >= 'A' && source.charAt(i) <= 'Z')||(source.charAt(i) >= '0' && source.charAt(i) <= '9')){
                        sb.append(var1).append(source.charAt(i));
                        //sb1.append(var3).append(source.charAt(i));
                        //System.out.println(sb);
                        //System.out.println("num-:"+source.charAt(i));
                        //System.out.println("i-: "+ i);


                        int temp1 = i;//Revisa que no se paso del final, despues de lo que ya aregramos.
                        temp1++;
                        if((source.charAt(temp1) >= 'a' && source.charAt(temp1) <= 'z') || (source.charAt(temp1) >= 'A' && source.charAt(temp1) <= 'Z')||(source.charAt(temp1) >= '0' && source.charAt(temp1) <= '9'))
                            i++;
                        else {
                            //i++;
                            break;
                        }
                    }else{
                        break;
                    }
                }

                //sb.append(var1).append("\"");
                String var2 = sb.toString();

                //tokens.add(new Token(cadena.get("\""),var2,sb1,linea));

                //System.out.println("asd: "+palabrasReservadas.containsKey(var2));

                if(palabrasReservadas.containsKey(var2)){
                    tokens.add(new Token(palabrasReservadas.get(var2),var2,null,linea));
                }else{
                    tokens.add(new Token(identificador.get("\"\""),var2,null,linea));
                }
            }


            //if(source.charAt(i) == ' ') i++;

            int temp1 = i;
            //temp1++;
            if(temp1 < source.length()){
                temp1++;
                if(temp1 == source.length())
                    i++;
                else
                    i++;
            }



        }

        //System.out.println(source);

        tokens.add(new Token(TipoToken.EOF, "", null, linea));
        return tokens;
    }
}

/*
Signos o sÃ­mbolos del lenguaje:
(mm
)mm
{mm
}mm
,mm
.mm
;mm
-mm
+mm
*mm
/mm
!mm
!=mm
=mm
==mm
<mm
<=//
>//
>=//
// -> comentarios (no se genera token)mm
/* ... * / -> comentarios (no se genera token)mm
Identificador,
Cadena
Numero
Cada palabra reservada tiene su nombre de token

 */
