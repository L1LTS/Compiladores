
public enum TipoToken {
    // Crear un tipoToken por palabra reservada
    // Crear un tipoToken: identificador, una cadena y numero
    // Crear un tipoToken por cada "Signo del lenguaje" (ver clase Scanner)

    //Identificador
    IDENTIFICADOR,

    //Operadores Relacionales
    EQ, LE, GE, LL, GG, NE, EQEQ, NN, NET,

    //Simbolos
    IZQ_PAR, DER_PAR,DER_LLAVE,IZQ_LLAVE,COMMA,DOT,SEMICOLON,DASH,ADD,ASTERISK,SLASH,

    // Palabras clave:
    IF, CLASS, ELSE, FOR, PRINT, RETURN, TRUE, FALSE, WHILE, NULL, AND, FUN, OR, SUPER, THIS, VAR,

    //Cadena
    CADENA,

    //Comentario
    COMMENT,COMMENTCOMMENT,

    // Final de cadena
    EOF
}

