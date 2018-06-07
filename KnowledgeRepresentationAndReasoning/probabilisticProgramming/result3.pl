0.01::visitedAsia.
0.3::smoker.
0.474470459726::tubercolosis :- visitedAsia.
0.774679825843::tubercolosis.
0.7::lung_cancer :- smoker.
0.902780168623::lung_cancer.
0.4::bronchitis :- smoker.
0.699075409021::bronchitis.
0.378188728545::tuberculois_or_cancer :- lung_cancer.
0.890797196799::tuberculois_or_cancer :- tubercolosis.
0.3::dyspnea :- tuberculois_or_cancer.
0.2::dyspnea :- bronchitis.
0.6::dyspnea.
0.495763719073::xray_positive :- tuberculois_or_cancer.
0.663644523318::xray_positive.
result :- smoker, xray_positive, lung_cancer.
