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
### 2. Use timestamp instead of bigint
```
`event_time` timestamp NOT NULL,
```
### 3. Use int instead of tinyint for sustainable enum
```
`op_type` int NOT NULL COMMENT '1: DEPOSIT 2: WITHDRAW 4: CLAIM 5: TRANSFER_OUT 6: TRANSFER_IN',
```