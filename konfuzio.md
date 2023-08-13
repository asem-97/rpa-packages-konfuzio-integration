1. The process aims to :
    1.1 Extract the category of the uploaded document.
    1.2 Extract the metadata(Array of labeles each label has name,value and accuracy).
1. In UploadDocument API :
    We need the id from the response for Get Document API.
2. The response of Get Document API(What matters for me) :
    2.1 category field (To get the name of the category from the third API).
    2.2 annotation_sets field (Array of ClusteredLabeles objects ):
        2.2.1: Each ClusteredLabeles object contains id,label_set object and array of labels objects.
            2.2.1.1: label_set object(describes the cluster) we need the name from it let's say ((X)).
            2.2.1.2: Each Label object contais the following : name field and array field called annotations , The Label type is ((X)) and we need to iterate over annotations array to get the annotation object that has the higher confidence.
                2.2.1.2.1: Each Annotation object has : confidence(accuracy) field and normalized value field.
Questions :
1. what about has_multiple_annotation_sets field in label_set object ?
    if true then build a Table !
    
2. what about has_multiple_top_candidates in each Label object?
    if true the label x the value is array of annotions that has the confidence greater>90(Ask Moath) ((Y))
    Moath: The Greatest one! 
3. We achieve the first goal 1.1 through 2.1 ?
4. We acheive the second goal 1.2 as follows :
    Each Label has name: we get it from 2.2.1.2 directly.
    Each Label has value and accuracy: we get them from iterating over the internal annotations array to pick the one that has the higher confidence.
5. What's the use of label_set object[2.2.1.1] from programming point of view,do I need ((X)) in the output ?
6. Normalized field types, how to deal with?
    we have an APi to get the type of the label ((Y))



{
    "category": 16267,
    "annotation_sets": [
        {
            "id": 1850102,
            "label_set": {
                "id": 16267,
                "name": "ReceiveNote",
                "api_name": "ReceiveNote",
                "description": "",
                "has_multiple_annotation_sets": false
            },
            "labels": [
                {
                    "name": "to",
                    "has_multiple_top_candidates": false,
                    "annotations": [
                        {
                            "normalized": "الهندسية",
                            "confidence": 0.97,
                        }
                    ]
                },
                {
                    "name": "number",
                    "has_multiple_top_candidates": false,
                    "annotations": [
                        {
                            "normalized": 191,
                            "confidence": 0.98,
                        }
                    ]
                },
                {
                    "name": "from",
                    "has_multiple_top_candidates": false,
                    "annotations": [
                        {
                            "normalized": "الانمائية",
                            "confidence": 1,
                        }
                    ]
                }
            ]
        }
    ],

}



Multiple Sets :

{
"category": 16666,
"annotation_sets": [
        {
            "id": 3855142,
            "label_set": {
                "id": 16666,
                "name": "ReOrderPayment",
                "api_name": "ReOrderPayment",
                "description": "",
                "has_multiple_annotation_sets": false
            },
            "labels": [
                {
                    "name": "EmployeeName",
                    "has_multiple_top_candidates": false,
                    "annotations": [
                        {
                            "normalized": "معاذ محمد علي المالكي",
                            "confidence": 0.6525,
                        }
                    ]
                },
                {
                    "name": "GovermentName",
                    "has_multiple_top_candidates": false,
                    "annotations": [
                        {
                            "normalized": "وزارة الشؤون البلدية والقروية والإسكان",
                            "confidence": 1.0,
                        }
                    ]
                },
                {
                    "name": "IdNumber",
                    "has_multiple_top_candidates": false,
                    "annotations": [
                        {
                            "normalized": "٢٢٦٧٩٨٤٢٥",
                            "confidence": 0.9,
                        }
                    ]
                },
                {
                    "name": "JobTitle",
                    "has_multiple_top_candidates": false,
                    "annotations": [
                        {
                            "normalized": "مدير تقني",
                            "confidence": 0.975,
                        }
                    ]
                },
                {
                    "name": "PhoneNumber",
                    "has_multiple_top_candidates": false,
                    "annotations": [
                        {
                            "normalized": "٤ ٠ ٠٥٥٧٠٨٧٨",
                            "confidence": 0.9033333333333333,
                        }
                    ]
                }
            ]
        },
        {
            "id": 3855143,
            "label_set": {
                "id": 16667,
                "name": "PaymentOrders",
                "api_name": "reorderpayment:paymentOrders",
                "description": "",
                "has_multiple_annotation_sets": true
            },
            "labels": [
                {
                    "name": "OrderDate",
                    "has_multiple_top_candidates": false,
                    "annotations": [
                        {
                            "normalized": "٢٠٢١-١٢-٠٢",
                            "confidence": 0.99,
                        }
                    ]
                },
                {
                    "name": "PaymentNumber",
                    "has_multiple_top_candidates": false,
                    "annotations": [
                        {
                            "normalized": "٨٦٢٣٠",
                            "confidence": 0.99,
                        }
                    ]
                },
                {
                    "name": "PaymentOrderdate",
                    "has_multiple_top_candidates": false,
                    "annotations": [
                        {
                            "normalized": "٢٠٢١-١٢-٠٥",
                            "confidence": 0.97,
                        }
                    ]
                },
                {
                    "name": "SupplierName",
                    "has_multiple_top_candidates": false,
                    "annotations": [
                        {
                            "normalized": "علي محمد الغامدي",
                            "confidence": 0.8666666666666667,
                        }
                    ]
                },
                {
                    "name": "SupplierNumber",
                    "has_multiple_top_candidates": false,
                    "annotations": [
                        {
                            "normalized": "٣٩٨٧٧",
                            "confidence": 0.97,
                        }
                    ]
                }
            ]
        },
        {
            "id": 3855144,
            "label_set": {
                "id": 16667,
                "name": "PaymentOrders",
                "api_name": "reorderpayment:paymentOrders",
                "description": "",
                "has_multiple_annotation_sets": true
            },
            "labels": [
                {
                    "name": "OrderDate",
                    "has_multiple_top_candidates": false,
                    "annotations": [
                        {
                            "normalized": "٢٠٢١-١٢-٠٢",
                            "confidence": 1.0,
                        }
                    ]
                },
                {
                    "id": 29761,
                    "name": "PaymentNumber",
                    "api_name": "paymentNumber",
                    "description": "",
                    "has_multiple_top_candidates": false,
                    "annotations": []
                },
                {
                    "name": "PaymentOrderdate",
                    "has_multiple_top_candidates": false,
                    "annotations": [
                        {
                            "normalized": "٢٠٢١-١٢-٠٥",
                            "confidence": 1.0,
                        }
                    ]
                },
                {
                    "name": "SupplierName",
                    "has_multiple_top_candidates": false,
                    "annotations": [
                        {
                            "normalized": "ماهر محمد عيد الردادي",
                            "confidence": 0.9975,
                        }
                    ]
                },
                {
                    "name": "SupplierNumber",
                    "has_multiple_top_candidates": false,
                    "annotations": [
                        {
                            "normalized": "٣٩٨٧٨",
                            "confidence": 1.0,
                        }
                    ]
                }
            ]
        },
        {
            "id": 3855145,
            "label_set": {
                "id": 16667,
                "name": "PaymentOrders",
                "api_name": "reorderpayment:paymentOrders",
                "description": "",
                "has_multiple_annotation_sets": true
            },
            "labels": [
                {
                    "name": "OrderDate",
                    "has_multiple_top_candidates": false,
                    "annotations": [
                        {
                            "normalized": "٢٠٢١-١٢-٠٢",
                            "confidence": 1.0,
                        }
                    ]
                },
                {
                    "name": "PaymentNumber",
                    "has_multiple_top_candidates": false,
                    "annotations": [
                        {
                            "normalized": "٨٦٢٣٢",
                            "confidence": 1.0,
                        }
                    ]
                },
                {
                    "name": "PaymentOrderdate",
                    "has_multiple_top_candidates": false,
                    "annotations": [
                        {
                            "normalized": "٢٠٢١-١٢-٠٥",
                            "confidence": 1.0,
                        }
                    ]
                },
                {
                    "name": "SupplierName",
                    "has_multiple_top_candidates": false,
                    "annotations": [
                        {
                            "normalized": "ماجد محمد عوض البلوي",
                            "confidence": 1.0,
                        }
                    ]
                },
                {
                    "name": "SupplierNumber",
                    "annotations": [
                        {
                            "normalized": "٣٩٨٧٩",
                            "confidence": 1.0,

                        }
                    ]
                }
            ]
        }
    ],
}




What if the label doesnt hava annotations at all ?

                

