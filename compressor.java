import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;


public class compressor {
    public static void main(String[] args) {
        // Ruta del archivo de audio de entrada
        String inputFile = "assets/campanes10s.wav";
        // Número de muestras por bloque
        int blockSize = 512; // Por ejemplo, 512 muestras por bloque

        // Llamada a la función de preprocesamiento
        try {
            preprocessAudio(inputFile, blockSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void preprocessAudio(String inputFile, int blockSize) throws Exception {
        // Cargar el archivo de audio
        File audioFile = new File(inputFile);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);

        // Obtener la información del formato de audio
        AudioFormat format = audioInputStream.getFormat();
        System.out.println("Formato de audio: "+format);
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

            // Ejemplo: Imprimir el número de bytes leídos en este bloque
            System.out.println("Bytes leídos: " + bytesRead);
        }

        // Cerrar el flujo de entrada de audio
        audioInputStream.close();
    }

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
}
