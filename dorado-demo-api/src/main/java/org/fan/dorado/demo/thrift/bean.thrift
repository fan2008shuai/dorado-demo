namespace java org.fan.dorado.demo.bean

struct Other {
    1: required string address;
    2: required string phoneNumber;
}

struct User {
    1: required string name;
    2: optional i16 age = 0;
    3: bool gender;
    4: Other other;
}

struct Response {
    1: required i16 ec;
    2: string em;
}