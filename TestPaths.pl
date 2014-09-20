#Simple perl script to run CloseEdge to find the shortest paths of all of the starting points

$filename = $ARGV[1];
@totals = [$ARGV[0]];
for($i=1;$i<=$ARGV[0];$i++){
	#print $i;
	$total = `java ClosestEdge $filename $i`;
	print "Starting at $i the distance is: ", $total;
	$totals[$i] = $total;
}

#print join(", ", @totals);
$shortest = 100000000000000;
#for(@totals){
	#print $_;
	#if($shortest > $_){
	#	$shortest = $_;
	#}
#}
$shortNode = -1;
for($i = 0; $i < $ARGV[0]; $i++){
	
	if($shortest > $totals[$i]){
		$shortest = $totals[$i];
		$shortNode = $i;
	}
}

print "The shortest distance is: $shortest starting at node $shortNode\n";