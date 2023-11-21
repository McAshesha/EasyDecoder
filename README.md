# EasyDecoder

EasyDecoder is a Java program designed for simple encoding and decoding of data packets using the Linear Feedback Shift Register (LFSR) algorithm. Basically, this process of decoding and encoding data packets is used in data transmission in the game "Mafia Online". Thanks to my program, you can easily view any data packages of this game!

## Table of Contents

- [Usage](#usage)
- [Options](#options)
- [Examples](#examples)
- [License](#license)

## Usage

1. Download the `EasyDecoder.jar` file from [EasyDecoder.jar](https://drive.google.com/uc?export=download&confirm=no_antivirus&id=1JIPxmLUs9sVrxRqLlEb0I3TDBPRjBqd0).

2. Open a terminal or command prompt.

3. Navigate to the directory where `EasyDecoder.jar` is located.

4. Run the following command to execute EasyDecoder with the desired options and arguments:

   ```bash
   java -jar EasyDecoder.jar [options]
   ```

## Options

- `-encode`: Encode the data packet.
- `-decode`: Decode the data packet.
- `-lfm=<value>`: Set the Linear Feedback Mask (LFM) with the specified integer value.
- `-in=<filename>`: Specify the input file name. Default is "input.json".
- `-out=<filename>`: Specify the output file name. Default is "output.json".

## Examples

1. Encode a packet with default options:

   ```bash
   java -jar EasyDecoder.jar -encode
   ```

2. Decode a packet with a custom LFM:

   ```bash
   java -jar EasyDecoder.jar -decode -lfm=123456
   ```

3. Encode a packet with custom input and output filenames:

   ```bash
   java -jar EasyDecoder.jar -encode -in=my_input_data -out=my_output_data
   ```

## License

EasyDecoder is distributed under the MIT License. See [LICENSE](LICENSE) for more information.

Feel free to contribute or report issues by creating pull requests or submitting bug reports. Happy coding with EasyDecoder!