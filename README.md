## Java Application Design
### 1. Use Instant instead of Date
```
long epoch = Long.parseLong(blockDTO.getTimestamp());
Instant instant = Instant.ofEpochSecond(epoch); // unit in s
```

## SQL Table Design
### 1. Default order by id DESC
```
PRIMARY KEY (id DESC),
KEY address_event_time (user_address, event_time DESC)
```
To alter tables, try:
```
ALTER TABLE strx_account_record
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id` DESC);
```
### 2. Use datetime instead of bigint
** Also set the MYSQL server time zone in connection string
```
`event_time` datetime NOT NULL,
jdbc-url: jdbc:mysql://...&serverTimezone=UTC
```
### 3. Use tinyint instead of int for enum
```
`op_type` tinyint NOT NULL COMMENT '1: DEPOSIT 2: WITHDRAW 4: CLAIM 5: TRANSFER_OUT 6: TRANSFER_IN',
```