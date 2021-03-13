FROM alpine:3.8

RUN echo "0 2 * * * find /gnoc/*.log.*.gz -type f -mtime +3 -delete" > /etc/crontabs/root

CMD ["crond", "-f"]
