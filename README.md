# fill & ccl

* Flood fill, four-way. https://en.wikipedia.org/wiki/Flood_fill
* Connected component labeling, 8-connectivity

The program generates requests for flood fill or CCL. The request contains
width, height, color range, 2d array of numbers, steps to run on the array and
an expected 2d array of number.

## 8 connectivity

```
1 2 3
4 5 6
7 8 9
```

5 connected to 1 2 3 4.

## 6 connectivity

```
 1 2 3
4 5 6
 7 8 9
```

5 connected to 1 2 4.

## Usage

    $ java -jar fill-0.0.2-standalone.jar [args]

## Options

* -t | --type <fill | ccl> - which request to generate, fill or ccl
* -d | --dir <directory> - where to store generated request files
* -n | --num-request N - number of requests to generate
* --min-width N - minimal width of the 2d data array
* --max-width N - maximal width of the 2d data array
* --min-height N - minimal height of the 2d data array
* --max-height N - maximal height of the 2d data array
* --min-color-range N - minimal color range to use for data points
* --max-color-range N - maximal color range to use for data points
* --probability N - probability of duplicating adjacent points, 0...100
* --ccl-output-background STR - string to use as default in ccl for points without labels
* --pretty - pretty print requests

## Examples

Check 'run_n_check.sh' script to see usage example.

## Bugs

## License

Copyright © 2017

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
