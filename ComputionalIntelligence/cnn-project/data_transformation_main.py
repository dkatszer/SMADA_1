import traffic_data
from skimage.color import rgb2gray
import numpy as np

# THIS IS ONLY FOR PURPOSE OF DOCUMENTATION
images, labels = traffic_data.load_data(traffic_data.train_data_directory)

# Show how many images is in data set
traffic_data.hist_no_images_per_sign(labels)

# Show how many images is in data set
traffic_data.show_images_with_occurences(labels, images)

# Show fact that images have different size
print('Before resizing')
traffic_data.show_random_signs(images)
print('After resizing')
resizedImages = traffic_data.resize_images(images)
traffic_data.show_random_signs(resizedImages)

# Gray
grayImages = rgb2gray(np.array(resizedImages))
traffic_data.show_random_signs(grayImages, True)
