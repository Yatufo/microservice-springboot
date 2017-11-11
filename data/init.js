db.appUser.insert({
  "username": "admin",
  "password" : "698857fd90cd5638f37c40a84f0ba30c418431fafc3d427f003bf5595bac4b88599083e0fe9923b9",
  "roles" : [
    "ADMIN"
  ],
  "_class" : "com.timezones.model.AppUser"
})

db.appUser.insert({
    "username" : "anonymous",
    "password" : "698857fd90cd5638f37c40a84f0ba30c418431fafc3d427f003bf5595bac4b88599083e0fe9923b9",
    "roles" : [
        "ANONYMOUS"
    ],
    "createdBy" : "admin",
    "_class" : "com.timezones.model.AppUser"
})

db.appUser.insert({
    "username" : "user",
    "password" : "e820b71a8f03996f103a200b34fac4dcd2017932e0a96f46eb867c47d37925136e6531b285df0d06",
    "roles" : [
        "USER"
    ],
    "createdBy" : "admin",
    "_class" : "com.timezones.model.AppUser"
})

db.appUser.insert({
    "username" : "otheruser",
    "password" : "e820b71a8f03996f103a200b34fac4dcd2017932e0a96f46eb867c47d37925136e6531b285df0d06",
    "roles" : [
        "USER"
    ],
    "createdBy" : "admin",
    "_class" : "com.timezones.model.AppUser"
})

db.appUser.insert({
    "username" : "manager",
    "password" : "e820b71a8f03996f103a200b34fac4dcd2017932e0a96f46eb867c47d37925136e6531b285df0d06",
    "roles" : [
        "MANAGER"
    ],
    "createdBy" : "admin",
    "_class" : "com.timezones.model.AppUser"
})

db.appUser.insert({
    "username" : "othermanager",
    "password" : "e820b71a8f03996f103a200b34fac4dcd2017932e0a96f46eb867c47d37925136e6531b285df0d06",
    "roles" : [
        "MANAGER"
    ],
    "createdBy" : "admin",
    "_class" : "com.timezones.model.AppUser"
})
