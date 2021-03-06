#!/bin/bash

# script to run ccl generator and checker together

do_one_dir(){
	dir="$1"
	generator_params="-d $dir -n $number_of_requests \
		--min-width $min_width --max-width $max_width \
		--min-height $min_height --max-height $max_height \
		--probability $probability \
		--ccl-output-background -1 \
		-t ccl --connectivity $connectivity \
		--min-color-range $min_color_range --max-color-range $max_color_range"
	checker_params="--connectivity $connectivity --remove --indir $dir"
	$generator $generator_params
	$checker $checker_params
}

export RLOG_LOG_LEVEL=ERROR

generator="java -jar fill.jar"
checker="./ccl_g"
connectivity=6
number_of_requests=10
min_width=4
max_width=100
min_height=4
max_height=100
min_color_range=4
max_color_range=10
probability=50

start_dir="${1:-.}"
base="${start_dir}/req"

batches=10
n=0
while ((n<batches))
do
    echo "batch: $n, `date`"
    dir="${base}/$n"
    mkdir -p "$dir"
    do_one_dir "$dir"
    rmdir --ignore-fail-on-non-empty "$dir"
    if [ -d "$dir" ] ; then
        echo "some errors in batch: $n, dir: $dir"
    fi
    ((n += 1))
done

