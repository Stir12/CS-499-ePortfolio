from pymongo import MongoClient, errors
from bson.objectid import ObjectId
from bson.json_util import dumps


class AnimalShelter(object):
    """ CRUD operations for Animal collection in MongoDB """

    def __init__(self, username, password):
        # Initializing the MongoClient
        self.client = MongoClient('mongodb://%s:%s@localhost:39329/test?authSource=AAC' % (username, password))
        self.database = self.client["AAC"]

    def create(self, data):
        try:
            if data:
                insert = self.database.animals.insert_one(data)
                return insert.acknowledged
            else:
                raise ValueError("Nothing to save, data parameter is empty")
        except errors.PyMongoError as e:
            print("Create Operation Error:", e)
            return False

    def read(self, criteria=None):
        try:
            return self.database.animals.find(criteria if criteria else {}, {"_id": False})
        except errors.PyMongoError as e:
            print("Read Operation Error:", e)
            return None

    def update(self, initial, change):
        try:
            if initial and change:
                update_result = self.database.animals.update_many(initial, {"$set": change})
                return update_result.raw_result
            else:
                raise ValueError("Invalid parameters for update")
        except errors.PyMongoError as e:
            print("Update Operation Error:", e)
            return None

    def delete(self, remove):
        try:
            if remove:
                delete_result = self.database.animals.delete_many(remove)
                return delete_result.raw_result
            else:
                raise ValueError("Invalid delete criteria")
        except errors.PyMongoError as e:
            print("Delete Operation Error:", e)
            return None

    def aggregate_data(self, criteria):
        try:
            if criteria:
                return self.database.animals.aggregate(criteria)
            else:
                raise ValueError("Invalid input for aggregation")
        except errors.PyMongoError as e:
            print("Aggregation Error:", e)
            return None

    def create_index(self, field_name):
        try:
            if field_name:
                self.database.animals.create_index(field_name)
                print(f"Index created on {field_name}")
            else:
                raise ValueError("Invalid field name for indexing")
        except errors.PyMongoError as e:
            print("Index Creation Error:", e)

    def dynamic_query_builder(self, query_parameters):
        try:
            if query_parameters:
                query = {param.key: param.value for param in query_parameters}
                return query
            else:
                raise ValueError("Invalid query parameters")
        except Exception as e:
            print("Dynamic Query Builder Error:", e)
            return None

    def execute_crud_operation(self, operation_type, parameters):
        try:
            operations = {
                "create": lambda p: self.create(p),
                "read": lambda p: self.read(p),
                "update": lambda p: self.update(p.get("initial"), p.get("change")),
                "delete": lambda p: self.delete(p)
            }
            if operation_type in operations:
                return operations[operation_type](parameters)
            else:
                raise ValueError("Invalid CRUD operation type")
        except errors.PyMongoError as e:
            print("CRUD Operation Error:", e)
            return None