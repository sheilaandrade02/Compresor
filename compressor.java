import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class compressor {
    public static void main(String[] args) {
        // Ruta del archivo de audio de entrada
        String inputFile = "assets/campanes10s.wav";

        // Número de muestras por bloque
        int blockSize = 1000; // Por ejemplo, 512 muestras por bloque

        // Umbral para determinar el silencio (ajústalo según sea necesario)
        double silenceThreshold = 0.1;

        // Llamada a la función de preprocesamiento
        try {
            preprocessAudio(inputFile, blockSize, silenceThreshold);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void preprocessAudio(String inputFile, int blockSize, double silenceThreshold) throws Exception {
        // Cargar el archivo de audio
        File audioFile = new File(inputFile);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);

        // Obtener la información del formato de audio
        AudioFormat format = audioInputStream.getFormat();
        System.out.println("Formato de audio: " + format);

        // Verificar si el formato es PCM
        if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
            throw new IllegalArgumentException("El archivo de audio debe estar en formato PCM");
        }

        // Definir un buffer para leer las muestras del archivo de audio
        byte[] buffer = new byte[blockSize * format.getFrameSize()];

        // Leer el audio por bloques
        int bytesRead;
        while ((bytesRead = audioInputStream.read(buffer)) != -1) {
            // Procesar el bloque de muestras aquí
            // buffer contiene las muestras de audio leídas

            // Verificar si el bloque es silencioso
            boolean isSilent = isSilent(buffer, silenceThreshold);

            if (isSilent) {
                System.out.println("Bloque de audio silencioso");
            } else {
                System.out.println("Bloque de audio no silencioso");

                // Aplicar la Transformada Coseno Discreta (DCT) al bloque
                double[] dctCoefficients = applyDCT(buffer);

                // Cuantificar los coeficientes de la DCT
                double[] quantizedCoefficients = quantize(dctCoefficients);

                // Hacer algo con los coeficientes cuantificados, por ejemplo, imprimirlos
                System.out.println("Coeficientes cuantificados:");
                for (double coefficient : quantizedCoefficients) {
                    System.out.print(coefficient + " ");
                }
                System.out.println();
            }
        }

        // Cerrar el flujo de entrada de audio
        audioInputStream.close();
    }

    public static boolean isSilent(byte[] buffer, double silenceThreshold) {
        // Calcular el promedio de las amplitudes de las muestras en el bloque
        double sum = 0.0;
        for (byte sample : buffer) {
            // Convertir el byte a un valor de amplitud en el rango [-1, 1]
            double amplitude = (double) sample / Byte.MAX_VALUE;
            sum += Math.abs(amplitude);
        }
        double averageAmplitude = sum / buffer.length;

        // Verificar si el promedio de amplitudes está por debajo del umbral de silencio
        return averageAmplitude < silenceThreshold;
    }
    public static double[] applyDCT(byte[] buffer) {
        int N = buffer.length;
        double[] dctCoefficients = new double[N];

        // Aplicar la DCT al bloque de muestras
        for (int k = 0; k < N; k++) {
            double sum = 0.0;
            for (int n = 0; n < N; n++) {
                sum += buffer[n] * Math.cos(Math.PI * k * (2 * n + 1) / (2.0 * N));
            }
            dctCoefficients[k] = sum;
        }

        return dctCoefficients;
    }
    public static double[] quantize(double[] dctCoefficients) {
        // Aquí implementarías la cuantificación de los coeficientes de la DCT
        // Por ejemplo, podrías dividir cada coeficiente por un valor de cuantificación específico y luego redondearlo al entero más cercano
        // Esto reducirá la precisión de los coeficientes
        double[] quantizedCoefficients = new double[dctCoefficients.length];
        for (int i = 0; i < dctCoefficients.length; i++) {
            quantizedCoefficients[i] = Math.round(dctCoefficients[i]); // Ejemplo de cuantificación: redondear al entero más cercano
        }
        return quantizedCoefficients;
    }
}
/* 
    public static double[] dct(double[] input) {
        int N = input.length;
        double[] output = new double[N];

        for (int k = 0; k < N; k++) {
            double sum = 0.0;
            for (int n = 0; n < N; n++) {
                sum += input[n] * Math.cos((Math.PI * (2 * n + 1) * k) / (2.0 * N));
            }
            if (k == 0) {
                output[k] = sum * Math.sqrt(1.0 / N);
            } else {
                output[k] = sum * Math.sqrt(2.0 / N);
            }
        }

        return output;
    }
}*/