insert into common_gnoc.messages a(a.message_id, a.sms_gateway_id, 
a.content, a.sender_id, a.receiver_id, a.create_time, a.receiver_username,
a.receiver_phone, a.sent_time, a.status,
a.result, a.repeat, a.alias) values(
common_gnoc.messages_seq.nextval,:gate_id,:content,null,:receiver_id,
sysdate,:receiver_username,:receiver_phone,null,0,null,null,:alias)
