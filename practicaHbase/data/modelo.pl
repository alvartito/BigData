#!/usr/bin/perl

# fichero original
# 	NASDAQ_daily_prices_subset.csv
#
# formato
# 	exchange,stock_symbol,date,stock_price_open,stock_price_high,stock_price_low,stock_price_close,stock_volume,stock_price_adj_close

while (<STDIN>) {
	chomp();
	@a=split(",");
	$b=$a[2];
	$c=`date '+%s' -d '$b'`*1000;

	if ($ARGV[0] eq "RK=emp") {
		printf "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",$a[1],$a[3],$a[4],$a[5],$a[6],$a[7],$a[8],$c;
	} elsif ($ARGV[0] eq "RK=empUpd") {
		printf "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",$a[1],$a[3],$a[4],$a[5],$a[6],$a[7],$a[8],$c+60000;
	} elsif ($ARGV[0] eq "RK=empresa/TS") {
		printf "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",$a[1]."/".$a[2],$a[3],$a[4],$a[5],$a[6],$a[7],$a[8],$c;
	} elsif ($ARGV[0] eq "RK=empresa/TS-2") {
		printf "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",$a[1]."/".$a[2],$a[3],$a[4],$a[5],$a[6],$a[7],$a[8],$c;
	} elsif ($ARGV[0] eq "RK=metrica/TS") {
		$d=9223372036854775807-$c;
		printf "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n","open  /".$d,$a[1],$a[3],$c;
		printf "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n","high  /".$d,$a[1],$a[4],$c;
		printf "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n","low   /".$d,$a[1],$a[5],$c;
		printf "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n","close /".$d,$a[1],$a[6],$c;
		printf "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n","volume/".$d,$a[1],$a[7],$c;
		printf "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n","adj   /".$d,$a[1],$a[8],$c;
	} elsif ($ARGV[0] eq "RK=empresa/metrica/TS") {
		printf "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",$a[1]."/open  /".$a[2],$a[3],$c;
		printf "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",$a[1]."/high  /".$a[2],$a[4],$c;
		printf "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",$a[1]."/low   /".$a[2],$a[5],$c;
		printf "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",$a[1]."/close /".$a[2],$a[6],$c;
		printf "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",$a[1]."/volume/".$a[2],$a[7],$c;
		printf "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",$a[1]."/adj   /".$a[2],$a[8],$c;
	} else {
		die("Opción no reconocida");
	}
}
