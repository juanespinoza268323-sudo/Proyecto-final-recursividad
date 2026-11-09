public class EvaluadorMatematico {

    public static void main(String[] args) {
        String[] pruebas = {
            "7+10+2",
            "1+5*(7-1)+10",
            "5+7+(1+2*(8-9)+2)-10",
            "9-2*(75-9+(6+3+(1+2)*1)+7)+9",
            "5++1+10",
            "-5+10",
            "74-7/7",
            "10+9-A"
        };

        for (String p : pruebas) {
            try {
                int res = resolver(p);
                System.out.println(p + " = " + res);
            } catch (Exception e) {
                System.out.println(p + " -> Excepcion: " + e.getMessage());
            }
        }
    }

    public static int resolver(String expresion) throws Exception {
        if (expresion == null) {
            throw new Exception("La expresion no puede ser nula");
        }
        
        String s = expresion.replaceAll("\\s+", "");
        
        if (s.isEmpty()) {
            throw new Exception("La expresion esta vacia");
        }
        
        if (!s.matches("^[0-9+\\-*()]+$")) {
            throw new Exception("Error de formato: contiene caracteres no permitidos");
        }
        
        if (s.startsWith("+") || s.startsWith("-") || s.startsWith("*") ||
            s.endsWith("+") || s.endsWith("-") || s.endsWith("*") || s.endsWith("(")) {
            throw new Exception("Error de formato: operador o parentesis mal ubicado");
        }
        
        if (s.contains("++") || s.contains("--") || s.contains("**") || 
            s.contains("+-") || s.contains("-+") || s.contains("*+") || 
            s.contains("*-") || s.contains("-*") || s.contains("+*")) {
            throw new Exception("Error de formato: operadores consecutivos invalidos");
        }

        return evaluarRecursivo(s);
    }

    private static int evaluarRecursivo(String s) throws Exception {
        if (s.isEmpty()) {
            throw new Exception("Error de formato");
        }

        int profundidadParen = 0;

        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
            if (c == ')') profundidadParen++;
            else if (c == '(') profundidadParen--;
            else if (profundidadParen == 0 && (c == '+' || c == '-')) {
                String izquierda = s.substring(0, i);
                String derecha = s.substring(i + 1);
                
                if (izquierda.isEmpty() || derecha.isEmpty()) {
                    throw new Exception("Error de formato alrededor del operador");
                }
                
                int valIzq = evaluarRecursivo(izquierda);
                int valDer = evaluarRecursivo(derecha);
                
                return (c == '+') ? (valIzq + valDer) : (valIzq - valDer);
            }
        }

        profundidadParen = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
            if (c == ')') profundidadParen++;
            else if (c == '(') profundidadParen--;
            else if (profundidadParen == 0 && c == '*') {
                String izquierda = s.substring(0, i);
                String derecha = s.substring(i + 1);
                
                if (izquierda.isEmpty() || derecha.isEmpty()) {
                    throw new Exception("Error de formato alrededor del operador");
                }
                
                int valIzq = evaluarRecursivo(izquierda);
                int valDer = evaluarRecursivo(derecha);
                
                return valIzq * valDer;
            }
        }

        if (s.startsWith("(") && s.endsWith(")")) {
            int contadorParen = 0;
            boolean cierraAntes = false;
            for (int i = 0; i < s.length() - 1; i++) {
                if (s.charAt(i) == '(') contadorParen++;
                else if (s.charAt(i) == ')') contadorParen--;
                if (contadorParen == 0) {
                    cierraAntes = true;
                    break;
                }
            }
            if (!cierraAntes) {
                return evaluarRecursivo(s.substring(1, s.length() - 1));
            }
        }

        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new Exception("Número o formato inválido: " + s);
        }
    }
}
