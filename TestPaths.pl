#Simple perl script to run CloseEdge to find the shortest paths of all of the starting points

$filename = $ARGV[1];
@totals = [$ARGV[0]];
for($i=1;$i<=$ARGV[0];$i++){
	print $i;
	$total = `java ClosestEdge $filename $i`;
	print "Starting at $i the distance is: ", $total, "\n";
	$totals[$i] = $total;
}

#print join(", ", @totals);
$shortest = 100000000000000;
for(@totals){
	#print $_;
	if($shortest > $_){
		$shortest = $_;
	}
}

print $shortest;