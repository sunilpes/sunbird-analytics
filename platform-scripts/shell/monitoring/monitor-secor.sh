#!/bin/bash
log_file=$1
mType=$2
today=$(date "+%Y-%m-%d")
output_dir="/mnt/secor/reports"
#output_dir="/tmp"

if [ "$mType" == "me" ]; then
	title="Secor | Monitoring Report | $today | Derived Telemetry"
	output_fname="$output_dir/secor-me-monitor-$today.csv"
else
	title="Secor | Monitoring Report | $today | Raw Telemetry"
	output_fname="$output_dir/secor-raw-monitor-$today.csv"
fi

warn=`grep " WARN " $log_file`
errors=`grep " ERROR " $log_file`
upload=`grep " INFO  uploading file " $log_file`

#warn_count=`grep " WARN " $log_file | wc -l | bc`
upload_count=`grep " INFO  uploading file " $log_file | wc -l | bc`
errors_count=0
if [ "$errors" != "" ]; then
	errors_count=`grep " ERROR " $log_file | wc -l | bc`
fi

## Upload Info
file_names=""
while read -r line
do
	f=`sed 's/.*s3n:\(.*\).*/\1/' <<< "$line"`
	file_names+="s3:$f\n"
done <<< "$upload"

echo "## Logging errors "
file_content="Type,Message\n"
## Errors
while read -r line
do
	msg=`sed 's/.*ERROR \(.*\).*/\1/' <<< "$line"`
	file_content+="ERROR,$msg\n"
done <<< "$errors"
echo "## errors logged "

echo "## Logging warning "
##Warnings
warn_count=0
subStr="com.pinterest.secor.common.FileRegistry:156) WARN  No writer found for path /mnt/secor-"
if [ "$warn" != "" ]; then
	while read -r line
	do
		if [ "${line/$subStr}" = "$line" ] ; then
        	msg=`sed 's/.*WARN \(.*\).*/\1/' <<< "$line"`
            file_content+="WARN,$msg\n"
            warn_count=$((warn_count+1))
        fi
	done <<< "$warn"
fi
echo "## warnings logged "

echo "Warn Count: $warn_count"
echo "Upload Count: $upload_count"
echo "Errors Count: $errors_count"

echo -e $file_content > $output_fname

data='{"channel": "#analytics_monitoring", "username": "secor-monitor", "text":"*'$title'*\nFiles Uploaded: `'$upload_count'` \n Warnings: `'$warn_count'` \n Errors: `'$errors_count'` \n", "icon_emoji": ":ghost:"}'
echo $data
curl -X POST -H 'Content-Type: application/json' --data "$data" https://hooks.slack.com/services/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