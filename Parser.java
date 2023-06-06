import java.util.List;
import java.util.*;


public class Parser {

    private final List<Token> tokens;

    int linea = 1;

    //Identificador
    private final Token identificador = new Token(TipoToken.IDENTIFICADOR, "");

    private final Token finCadena = new Token(TipoToken.EOF, "");

    //Simbolos
    private final Token par_izq = new Token(TipoToken.IZQ_PAR, "(");
    private final Token par_der = new Token(TipoToken.DER_PAR, ")");
    private final Token llave_der = new Token(TipoToken.DER_LLAVE, "{");
    private final Token llave_izq = new Token(TipoToken.IZQ_LLAVE, "}");
    private final Token comma = new Token(TipoToken.COMMA, ",");
    private final Token dot = new Token(TipoToken.DOT, ".");
    private final Token semicolon = new Token(TipoToken.SEMICOLON, ";");
    private final Token dash = new Token(TipoToken.DASH, "-");
    private final Token add = new Token(TipoToken.ADD, "+");
    private final Token asterisco = new Token(TipoToken.ASTERISK, "*");
    private final Token slash = new Token(TipoToken.SLASH, "/");

    //Palabras Reservadas
    private final Token if_token = new Token(TipoToken.IF, "if");
    private final Token class_token = new Token(TipoToken.CLASS, "class");
    private final Token else_token = new Token(TipoToken.ELSE, "else");
    private final Token for_token = new Token(TipoToken.FOR, "for");
    private final Token print_token = new Token(TipoToken.PRINT, "print");
    private final Token return_token = new Token(TipoToken.RETURN, "return");
    private final Token false_token = new Token(TipoToken.FALSE, "false");
    private final Token true_token = new Token(TipoToken.TRUE, "true");
    private final Token while_token = new Token(TipoToken.WHILE, "while");
    private final Token null_token = new Token(TipoToken.NULL, "null");
    private final Token and_token = new Token(TipoToken.AND, "and");
    private final Token fun_token = new Token(TipoToken.FUN, "fun");
    private final Token or_token = new Token(TipoToken.OR, "or");
    private final Token super_token = new Token(TipoToken.SUPER, "super");
    private final Token this_token = new Token(TipoToken.THIS, "this");
    private final Token var_token = new Token(TipoToken.VAR, "var");

    //Numero
    private final Token number = new Token(TipoToken.NUMERO, "#");

    //Operadores relacionales
    private final Token le_token = new Token(TipoToken.LE, "<=");
    private final Token ge_token = new Token(TipoToken.GE, ">=");
    private final Token ll_token = new Token(TipoToken.LL, ">");
    private final Token gg_token = new Token(TipoToken.GG, "<");
    private final Token ner_token = new Token(TipoToken.NE, "<>");
    private final Token eq_token = new Token(TipoToken.EQ, "==");
    private final Token eqeq_token = new Token(TipoToken.EQEQ, "=");
    private final Token nn_token = new Token(TipoToken.NN, "!");
    private final Token net_token = new Token(TipoToken.NET, "!=");

    //Cadenas
    private final Token cadena = new Token(TipoToken.CADENA, "\"");

    //Comentarios
    private final Token comment = new Token(TipoToken.COMMENT, "//");
    private final Token commentcomment = new Token(TipoToken.COMMENTCOMMENT, "/*");

    private int i = 0;
    private boolean hayErrores = false;

    private Token preanalisis;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    public void parse(){
        i = 0;
        preanalisis = tokens.get(i);
        //System.out.println(tokens);
        PROGRAM();

        if(!hayErrores && !preanalisis.equals(finCadena)){
            System.out.println("Error en la posición " + preanalisis.linea + ". No se esperaba el token " + preanalisis.tipo);
        }
        else if(!hayErrores && preanalisis.equals(finCadena)){
            System.out.println("Consulta válida");
        }
    }
    
    void PROGRAM(){
        DECLARATION();
    }

    //Declaraciones
    void DECLARATION(){
        //System.out.println("DECLARATION--");
        if(hayErrores) return;
        //System.out.println("DECLARATION");
        //System.out.println(preanalisis);
        //System.out.println("asdf"+ preanalisis.equals(number));
        if(preanalisis.equals(class_token)){
            CLASS_DECL();
            DECLARATION();
        } else if(preanalisis.equals(fun_token)){
            FUN_DECL();
            DECLARATION();
        }else if(preanalisis.equals(var_token)){
            VAR_DECL();
            DECLARATION();
        }else if(preanalisis.equals(nn_token) || preanalisis.equals(dash) || preanalisis.equals(true_token) || preanalisis.equals(false_token) || preanalisis.equals(null_token) || preanalisis.equals(this_token) || preanalisis.equals(number) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(par_der) || preanalisis.equals(super_token) || preanalisis.equals(for_token) || preanalisis.equals(if_token) || preanalisis.equals(print_token) || preanalisis.equals(return_token) || preanalisis.equals(while_token) || preanalisis.equals(llave_izq)){
            STATEMENT();
            DECLARATION();
        }
    }

    void CLASS_DECL(){
        if(hayErrores) return;
        //System.out.println("CLASS_DECL");
        //System.out.println(preanalisis);
        if(preanalisis.equals(class_token)) {
            coincidir(class_token);
            coincidir(identificador);
            CLASS_INHER();
            coincidir(llave_der);
            FUNCTIONS();
            coincidir(llave_izq);
        }else{
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un CLASS");
            System.out.println("Error en la consulta.");
        }
    }

    void CLASS_INHER(){
        if(hayErrores) return;
        //System.out.println("CLASS_INHER");
        //System.out.println(preanalisis);
        if(preanalisis.equals(gg_token)) {
            coincidir(gg_token);
            coincidir(identificador);
        }
    }

    void FUN_DECL(){
        if(hayErrores) return;
        //System.out.println("FUN_DECL");
        //System.out.println(preanalisis);
        if(preanalisis.equals(fun_token)) {
            coincidir(fun_token);
            FUNCTION();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un FUN");
        }
    }

    void VAR_DECL(){
        if(hayErrores) return;
        //System.out.println("VAR_DECL");
        //System.out.println(preanalisis);
        if(preanalisis.equals(var_token)) {
            coincidir(var_token);
            coincidir(identificador);
            VAR_INIT();
            coincidir(semicolon);
        }else{
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un VAR");
            System.out.println("Error en la consulta.");
        }
    }

    void VAR_INIT(){
        if(hayErrores) return;
        //System.out.println("VAR_INIT");
        //System.out.println(preanalisis);
        if(preanalisis.equals(eqeq_token)) {
            coincidir(eqeq_token);
            EXPRESSION();
        }
    }

    //Sentencias
    void STATEMENT(){
        if(hayErrores) return;
        //System.out.println("STATEMENT");
        //System.out.println(preanalisis);
        if(preanalisis.equals(nn_token) || preanalisis.equals(dash) || preanalisis.equals(true_token) || preanalisis.equals(false_token) || preanalisis.equals(null_token) || preanalisis.equals(this_token) || preanalisis.equals(number) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(par_der) || preanalisis.equals(super_token)) {
            EXPR_STMT();
        }else if(preanalisis.equals(for_token)) {
            FOR_STMT();
        }else if(preanalisis.equals(if_token)){
            IF_STMT();
        }else if(preanalisis.equals(print_token)){
            PRINT_STMT();
        }else if(preanalisis.equals(return_token)){
            RETURN_STMT();
        }else if(preanalisis.equals(while_token)){
            WHILE_STMT();
        }else if(preanalisis.equals(llave_izq)){
            BLOCK();
        }else{
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.linea + ".");
            System.out.println("Error en la consulta.");
        }
    }

    void EXPR_STMT(){
        if(hayErrores) return;
        //System.out.println("EXPR_STMT");
        //System.out.println(preanalisis);
        if(preanalisis.equals(nn_token) || preanalisis.equals(dash) || preanalisis.equals(true_token) || preanalisis.equals(false_token) || preanalisis.equals(null_token) || preanalisis.equals(this_token) || preanalisis.equals(number) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(par_der) || preanalisis.equals(super_token)) {
            EXPRESSION();
            coincidir(semicolon);
        }else{
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba UNA  EXPRESSION");
            System.out.println("Error en la consulta.");
        }
    }

    void FOR_STMT(){
        if(hayErrores) return;
        //System.out.println("FOR_STMT");
        //System.out.println(preanalisis);
        if(preanalisis.equals(for_token)) {
            coincidir(for_token);
            coincidir(par_izq);
            FOR_STMT_1();
            FOR_STMT_2();
            FOR_STMT_3();
            coincidir(par_der);
            STATEMENT();
        }else{
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un STATEMENT");
            System.out.println("Error en la consulta.");
        }
    }

    void FOR_STMT_1(){
        if(hayErrores) return;
        //System.out.println("FOR_STMT_1");
        //System.out.println(preanalisis);
        if(preanalisis.equals(var_token)) {
            VAR_DECL();
        }else if(preanalisis.equals(var_token) || preanalisis.equals(semicolon) || preanalisis.equals(nn_token) || preanalisis.equals(dash) || preanalisis.equals(true_token) || preanalisis.equals(false_token) || preanalisis.equals(null_token) || preanalisis.equals(this_token) || preanalisis.equals(number) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(par_der) || preanalisis.equals(super_token)) {
            EXPRESSION();
        }else if(preanalisis.equals(semicolon)){
            coincidir(semicolon);
        }else{
            hayErrores = true;
            System.out.println("Error en la consulta.");
        }
    }
    void FOR_STMT_2(){
        if(hayErrores) return;
        //System.out.println("FOR_STMT_2");
        //System.out.println(preanalisis);
        if(preanalisis.equals(semicolon) || preanalisis.equals(nn_token) || preanalisis.equals(dash) || preanalisis.equals(true_token) || preanalisis.equals(false_token) || preanalisis.equals(null_token) || preanalisis.equals(this_token) || preanalisis.equals(number) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(par_der) || preanalisis.equals(super_token)) {
            EXPRESSION();
            coincidir(semicolon);
        }else if(preanalisis.equals(semicolon)) {
            coincidir(semicolon);
        }else{
            hayErrores = true;
            System.out.println("Error en la consulta.");
        }
    }

    void FOR_STMT_3(){
        if(hayErrores) return;
        //System.out.println("FOR_STMT_3");
        //System.out.println(preanalisis);
        if(preanalisis.equals(nn_token) || preanalisis.equals(dash) || preanalisis.equals(true_token) || preanalisis.equals(false_token) || preanalisis.equals(null_token) || preanalisis.equals(this_token) || preanalisis.equals(number) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(par_der) || preanalisis.equals(super_token)) {
            EXPRESSION();
        }
    }

    void IF_STMT(){
        if(hayErrores) return;
        //System.out.println("IF_STMT");
        //System.out.println(preanalisis);
        if(preanalisis.equals(if_token)) {
            coincidir(if_token);
            coincidir(par_izq);
            EXPRESSION();
            coincidir(par_der);
            STATEMENT();
            ELSE_STATEMENT();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un STATEMENT");
        }
    }

    void ELSE_STATEMENT(){
        if(hayErrores) return;
        //System.out.println("ELSE_STATEMENT");
        //System.out.println(preanalisis);
        if(preanalisis.equals(else_token)) {
            coincidir(else_token);
            STATEMENT();
        }
    }

    void PRINT_STMT(){
        if(hayErrores) return;
        //System.out.println("PRINT_STMT");
        //System.out.println(preanalisis);
        //System.out.println(tokens);

        if(preanalisis.equals(print_token)) {
            coincidir(print_token);
            EXPRESSION();
            coincidir(semicolon);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un PRINT");
        }
    }

    void RETURN_STMT(){
        if(hayErrores) return;
        //System.out.println("RETURN_STMT");
        //System.out.println(preanalisis);
        if(preanalisis.equals(return_token)) {
            coincidir(return_token);
            RETURN_EXP_OPC();
            coincidir(semicolon);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un RETURN");
        }
    }

    void RETURN_EXP_OPC(){
        if(hayErrores) return;
        //System.out.println("RETURN_EXP_OPC");
        //System.out.println(preanalisis);
        if(preanalisis.equals(nn_token) || preanalisis.equals(dash) || preanalisis.equals(true_token) || preanalisis.equals(false_token) || preanalisis.equals(null_token) || preanalisis.equals(this_token) || preanalisis.equals(number) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(par_der) || preanalisis.equals(super_token)) {
            EXPRESSION();
        }
    }

    void WHILE_STMT(){
        if(hayErrores) return;
        //System.out.println("WHILE_STMT");
        //System.out.println(preanalisis);
        if(preanalisis.equals(while_token)) {
            coincidir(while_token);
            coincidir(par_izq);
            EXPRESSION();
            coincidir(par_der);
            STATEMENT();
        }else{
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un WHILE");
            System.out.println("Error en la consulta.");
        }
    }

    void BLOCK(){
        if(hayErrores) return;
        //System.out.println("BLOCK");
        //System.out.println(preanalisis);
        if(preanalisis.equals(llave_izq)) {
            coincidir(llave_izq);
            BLOCK_DECL();
            coincidir(llave_der );
        }else{
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un \"{\"");
            System.out.println("Error en la consulta.");
        }
    }

    void BLOCK_DECL(){
        if(hayErrores) return;
        //System.out.println("BLOCK_DECL");
        //System.out.println(preanalisis);

        if(preanalisis.equals(nn_token) || preanalisis.equals(dash) || preanalisis.equals(true_token) || preanalisis.equals(false_token) || preanalisis.equals(null_token) || preanalisis.equals(this_token) || preanalisis.equals(number) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(par_der) || preanalisis.equals(super_token) || preanalisis.equals(for_token) || preanalisis.equals(if_token) || preanalisis.equals(print_token) || preanalisis.equals(return_token) || preanalisis.equals(while_token) || preanalisis.equals(llave_izq)){
            DECLARATION();
            BLOCK_DECL();
        }
    }

    //Expresiones
    void EXPRESSION(){
        if(hayErrores) return;
        //System.out.println("EXPRESSION");
        //System.out.println(preanalisis);

        if(preanalisis.equals(nn_token) || preanalisis.equals(dash) || preanalisis.equals(true_token) || preanalisis.equals(false_token) || preanalisis.equals(null_token) || preanalisis.equals(this_token) || preanalisis.equals(number) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(par_der) || preanalisis.equals(super_token)) {
            ASSIGNMENT();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un ASSIGNMENT");
        }
    }

    void ASSIGNMENT(){
        if(hayErrores) return;
        //System.out.println("ASSIGNMENT");
        //System.out.println(preanalisis);
        if(preanalisis.equals(nn_token) || preanalisis.equals(dash) || preanalisis.equals(true_token) || preanalisis.equals(false_token) || preanalisis.equals(null_token) || preanalisis.equals(this_token) || preanalisis.equals(number) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(par_der) || preanalisis.equals(super_token)) {
            LOGIC_OR();
            ASSIGNMENT_OPC();
        }else{
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un ASSIGNMENT");
            System.out.println("Error en la consulta.");
        }
    }

    void ASSIGNMENT_OPC(){
        if(hayErrores) return;
        //System.out.println("ASSIGNMENT_OPC");
        //System.out.println(preanalisis);
        if(preanalisis.equals(eqeq_token)) {
            coincidir(eqeq_token);
            EXPRESSION();
        }
    }

    void LOGIC_OR() {
        if(hayErrores) return;
        //System.out.println("LOGIC_OR");
        //System.out.println(preanalisis);
        if(preanalisis.equals(nn_token) || preanalisis.equals(dash) || preanalisis.equals(true_token) || preanalisis.equals(false_token) || preanalisis.equals(null_token) || preanalisis.equals(this_token) || preanalisis.equals(number) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(par_der) || preanalisis.equals(super_token)){
            LOGIC_AND();
            LOGIC_OR_2();
        }else{
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.linea + ".");
            System.out.println("Error en la consulta.");
        }
    }

    void LOGIC_OR_2() {
        if(hayErrores)return;
        //System.out.println("LOGIC_OR_2");
        //System.out.println(preanalisis);
        if(preanalisis.equals(or_token)){
            coincidir(or_token);
            LOGIC_AND();
            LOGIC_OR_2();
        }
    }

    void LOGIC_AND() {
        if(hayErrores)return;
        //System.out.println("LOGIC_AND");
        //System.out.println(preanalisis);
        if(preanalisis.equals(nn_token) || preanalisis.equals(dash) || preanalisis.equals(true_token) || preanalisis.equals(false_token) || preanalisis.equals(null_token) || preanalisis.equals(this_token) || preanalisis.equals(number) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(par_der) || preanalisis.equals(super_token)){
            EQUALITY();
            LOGIC_AND_2();
        }else{
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un EQUALITY");
            System.out.println("Error en la consulta.");
        }
    }

    void LOGIC_AND_2() {
        if(hayErrores)return;
        //System.out.println("LOGIC_AND_2");
        //System.out.println(preanalisis);
        if(preanalisis.equals(and_token)){
            coincidir(and_token);
            EQUALITY();
            LOGIC_AND_2();
        }
    }

    void EQUALITY() {
        if(hayErrores)return;
        //System.out.println("EQUALITY");
        //System.out.println(preanalisis);
        if(preanalisis.equals(nn_token) || preanalisis.equals(dash) || preanalisis.equals(true_token) || preanalisis.equals(false_token) || preanalisis.equals(null_token) || preanalisis.equals(this_token) || preanalisis.equals(number) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(par_der) || preanalisis.equals(super_token)){
            COMPARISON();
            EQUALITY_2();
        }else{
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.linea + ".");
            System.out.println("Error en la consulta.");
        }
    }

    void EQUALITY_2() {
        if(hayErrores) return;
        //System.out.println("EQUALITY_2");
        //System.out.println(preanalisis);
        if(preanalisis.equals(net_token)){
            coincidir(net_token);
            COMPARISON();
            EQUALITY_2();
        }else if(preanalisis.equals(eq_token)) {
            coincidir(eq_token);
            COMPARISON();
            EQUALITY_2();
        }
    }

    void COMPARISON() {
        if(hayErrores) return;
        //System.out.println("COMPARISON");
        //System.out.println(preanalisis);
        if(preanalisis.equals(nn_token) || preanalisis.equals(dash) || preanalisis.equals(true_token) || preanalisis.equals(false_token) || preanalisis.equals(null_token) || preanalisis.equals(this_token) || preanalisis.equals(number) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(par_der) || preanalisis.equals(super_token)){
            TERM();
            COMPARISON_2();
        }else{
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un TERM");
            System.out.println("Error en la consulta.");
        }
    }

    void COMPARISON_2() {
        if(hayErrores) return;
        //System.out.println("COMPARISON_2");
        //System.out.println(preanalisis);
        if(preanalisis.equals(ll_token)){
            coincidir(ll_token);
            TERM();
            COMPARISON_2();
        }else if(preanalisis.equals(ge_token)){
            coincidir(ge_token);
            TERM();
            COMPARISON_2();
        }else if(preanalisis.equals(gg_token)){
            coincidir(gg_token);
            TERM();
            COMPARISON_2();
        }else if(preanalisis.equals(le_token)){
            coincidir(le_token);
            TERM();
            COMPARISON_2();
        }
    }

    void TERM() {
        if(hayErrores) return;
        //System.out.println("TERM");
        //System.out.println(preanalisis);
        if(preanalisis.equals(nn_token) || preanalisis.equals(dash) || preanalisis.equals(true_token) || preanalisis.equals(false_token) || preanalisis.equals(null_token) || preanalisis.equals(this_token) || preanalisis.equals(number) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(par_der) || preanalisis.equals(super_token)){
            FACTOR();
            TERM_2();
        }else{
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un FACTOR");
            System.out.println("Error en la consulta.");
        }
    }

    void TERM_2() {
        if(hayErrores) return;
        //System.out.println("TERM_2");
        //System.out.println(preanalisis);
        if(preanalisis.equals(add)){
            coincidir(add);
            FACTOR();
            TERM_2();
        }else if(preanalisis.equals(dash)){
            coincidir(dash);
            FACTOR();
            TERM_2();
        }
    }

    void FACTOR() {
        if(hayErrores) return;
        //System.out.println("FACTOR");
        //System.out.println(preanalisis);
        if(preanalisis.equals(nn_token) || preanalisis.equals(dash) || preanalisis.equals(true_token) || preanalisis.equals(false_token) || preanalisis.equals(null_token) || preanalisis.equals(this_token) || preanalisis.equals(number) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(par_der) || preanalisis.equals(super_token)){
            UNARY();
            FACTOR_2();
        }else{
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un UNARY");
            System.out.println("Error en la consulta.");

        }
    }

    void FACTOR_2() {
        if(hayErrores) return;
        //System.out.println("FACTOR_2");
        //System.out.println(preanalisis);
        if(preanalisis.equals(slash)){
            coincidir(slash);
            UNARY();
            FACTOR_2();
        }else if(preanalisis.equals(asterisco)){
            coincidir(asterisco);
            UNARY();
            FACTOR_2();
        }
    }

    void UNARY() {
        if(hayErrores) return;
        //System.out.println("UNARY");
        //System.out.println(preanalisis);
        if(preanalisis.equals(nn_token)){
            coincidir(nn_token);
            UNARY();
        }else if(preanalisis.equals(dash)){
            coincidir(dash);
            UNARY();
        }else if(preanalisis.equals(true_token) || preanalisis.equals(false_token) || preanalisis.equals(null_token) || preanalisis.equals(this_token) || preanalisis.equals(number) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(par_der) || preanalisis.equals(super_token)){
            CALL();
        }else{
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.linea + ".");
            System.out.println("Error en la consulta.");
        }
    }

    void CALL() {
        if(hayErrores) return;
        //System.out.println("CALL");
        //System.out.println(preanalisis);
        if(preanalisis.equals(true_token) || preanalisis.equals(false_token) || preanalisis.equals(null_token) || preanalisis.equals(this_token) || preanalisis.equals(number) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(par_der) || preanalisis.equals(super_token)){
            PRIMARY();
            CALL_2();
        }else{
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un PRYMARY");
            System.out.println("Error en la consulta.");
        }
    }

    void CALL_2() {
        if(hayErrores) return;
        //System.out.println("CALL_2");
        //System.out.println(preanalisis);
        if(preanalisis.equals(par_izq)){
            coincidir(par_izq);
            ARGUMENTS_OPC();
            coincidir(par_der);
            CALL_2();
        }else if(preanalisis.equals(dot)){
            coincidir(dot);
            coincidir(identificador);
            CALL_2();
        }
    }

    void CALL_OPC() {
        if(hayErrores) return;
        //System.out.println("CALL_OPC");
        //System.out.println(preanalisis);
        if(preanalisis.equals(true_token) || preanalisis.equals(false_token) || preanalisis.equals(null_token) || preanalisis.equals(this_token) || preanalisis.equals(number) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(par_der) || preanalisis.equals(super_token)){
            CALL();
            coincidir(dot);
        }
    }

    void PRIMARY() {
        if(hayErrores) return;
        //System.out.println("PRIMARY");
        //System.out.println(preanalisis);
        if(preanalisis.equals(true_token)){
            coincidir(true_token);
        }else if(preanalisis.equals(false_token)){
            coincidir(false_token);
        }else if(preanalisis.equals(null_token)){
            coincidir(null_token);
        }else if(preanalisis.equals(this_token)){
            coincidir(this_token);
        }else if(preanalisis.equals(number)){
            coincidir(number);
        }else if(preanalisis.equals(cadena)){
            coincidir(cadena);
        }else if(preanalisis.equals(identificador)){
            coincidir(identificador);
        }else if(preanalisis.equals(par_izq)){
            coincidir(par_izq);
            EXPRESSION();
            coincidir(par_der);
        }else if(preanalisis.equals(super_token)){
            coincidir(super_token);
            coincidir(dot);
            coincidir(identificador);
        }else{
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.linea + ".");
        }
    }

    //Otras
    void FUNCTION() {
        if(hayErrores) return;
        //System.out.println("FUNCTION");
        //System.out.println(preanalisis);
        if(preanalisis.equals(identificador)){
            coincidir(identificador);
            coincidir(par_izq);
            PARAMETERS_OPC();
            coincidir(par_der);
            BLOCK();
        }else{
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un IDENTIFICADOR");
            System.out.println("Error en la consulta.");
        }
    }

    void FUNCTIONS() {
        if(hayErrores) return;
        //System.out.println("FUNCTIONS");
        //System.out.println(preanalisis);
        if(preanalisis.equals(identificador)){
            FUNCTION();
            FUNCTIONS();
        }
    }

    void PARAMETERS_OPC() {
        if(hayErrores) return;
        //System.out.println("PARAMETERS_OPC");
        //System.out.println(preanalisis);
        if(preanalisis.equals(identificador)){
            PARAMETERS();
        }
    }

    void PARAMETERS() {
        if(hayErrores) return;
        //System.out.println("PARAMETERS");
        //System.out.println(preanalisis);
        if(preanalisis.equals(identificador)){
            coincidir(identificador);
            PARAMETERS_2();
        }else{
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un IDENTIFICADOR");
            System.out.println("Error en la consulta.");
        }
    }

    void PARAMETERS_2() {
        if(hayErrores) return;
        //System.out.println("PARAMETERS_2");
        //System.out.println(preanalisis);
        if(preanalisis.equals(comma)){
            coincidir(comma);
            coincidir(identificador);
            PARAMETERS_2();
        }
    }

    void ARGUMENTS_OPC() {
        if(hayErrores) return;
        //System.out.println("ARGUMENTS_OPC");
        //System.out.println(preanalisis);
        if(preanalisis.equals(true_token) || preanalisis.equals(false_token) || preanalisis.equals(null_token) || preanalisis.equals(this_token) || preanalisis.equals(number) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(par_der) || preanalisis.equals(super_token)){
            ARGUMENTS();
        }
    }

    void ARGUMENTS() {
        if(hayErrores) return;
        //System.out.println("ARGUMENTS");
        //System.out.println(preanalisis);
        if(preanalisis.equals(true_token) || preanalisis.equals(false_token) || preanalisis.equals(null_token) || preanalisis.equals(this_token) || preanalisis.equals(number) || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(par_der) || preanalisis.equals(super_token)){
            EXPRESSION();
            ARGUMENTS_2();
        }else{
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un TRUE");
            System.out.println("Error en la consulta.");
        }
    }

    void ARGUMENTS_2() {
        if(hayErrores) return;
        //System.out.println("ARGUMENTS_2");
        //System.out.println(preanalisis);
        if(preanalisis.equals(comma)){
            coincidir(comma);
            EXPRESSION();
            ARGUMENTS_2();
        }
    }

    void coincidir(Token t){
        if(hayErrores) return;
        //.out.println("COINCIDIR");
        //System.out.println(preanalisis.tipo);
        //System.out.println(t.tipo);

        if(preanalisis.tipo == t.tipo){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un  " + t.tipo);
            System.out.println("Error en la consulta.");

        }
    }

}
