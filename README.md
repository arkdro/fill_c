# fill

Flood fill, four-way.
https://en.wikipedia.org/wiki/Flood_fill

The program generates requests for flood fill. The request contains
width, height, color range, 2d array of numbers, steps to run on the array and
an expected 2d array of number.

## Usage

    $ java -jar fill-0.0.2-standalone.jar [args]

## Options

-d | --dir <directory> - where to store generated request files
-n | --num-request N - number of requests to generate
--min-width N - minimal width of the 2d data array
--max-width N - maximal width of the 2d data array
--min-height N - minimal height of the 2d data array
--max-height N - maximal height of the 2d data array
--min-color-range N - minimal color range to use for data points
--max-color-range N - maximal color range to use for data points
--probability N - probability of duplicating adjacent points, 0...100
--pretty - pretty print requests

## Examples

Check 'run_n_check.sh' script to see usage example.

### Bugs

## License

Copyright © 2017

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
