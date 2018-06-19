import os
from skimage import data, transform
import matplotlib.pyplot as plt
from skimage.color import rgb2gray
import numpy as np

ROOT_PATH = "traffic_lights"
train_data_directory = os.path.join(ROOT_PATH, "Training")
test_data_directory = os.path.join(ROOT_PATH, "Testing")


def load_data(data_directory):
    directories = [d for d in os.listdir(data_directory)
                   if os.path.isdir(os.path.join(data_directory, d))]
    labels = []
    images = []
    for d in directories:
        label_directory = os.path.join(data_directory, d)
        file_names = [os.path.join(label_directory, f)
                      for f in os.listdir(label_directory)
                      if f.endswith(".ppm")]
        for f in file_names:
            images.append(data.imread(f))
            labels.append(int(d))
    return images, labels


def hist_no_images_per_sign(labels):
    # Make a histogram with 62 bins of the `labels` data
    plt.hist(labels, len(set(labels)))
    plt.title('Distribution of the traffic signs')
    plt.xlabel('Traffic sing - label')
    plt.ylabel('Number of occurrences')
    # Show the plot
    plt.show()


def show_images_with_occurences(labels, images):
    unique_labels = set(labels)
    plt.figure(figsize=(15, 15))
    i = 1
    for label in unique_labels:
        # You pick the first image for each label
        image = images[labels.index(label)]
        # Define 64 subplots (8 rows and 8 columns)
        plt.subplot(8, 8, i)
        plt.axis('off')
        # Add a title to each subplot
        plt.title("Label {0} ({1})".format(label, labels.count(label)))
        # And you plot this first image
        plt.imshow(image)
        i += 1

    plt.show()


def show_random_signs(images, is_gray=False):
    traffic_signs = [300, 2250, 3650, 4000]
    # Fill out the subplots with the random images that you defined
    for i in range(len(traffic_signs)):
        plt.subplot(1, 4, i + 1)
        plt.axis('off')
        if not is_gray:
            plt.imshow(images[traffic_signs[i]])
        else:
            plt.imshow(images[traffic_signs[i]], cmap="gray")  # default is heatmap
        plt.subplots_adjust(wspace=0.5)
        print("shape(rows,columns,channels): {0}, min gray value: {1}, max gray value: {2}".format(
            images[traffic_signs[i]].shape,
            images[traffic_signs[i]].min(),
            images[traffic_signs[i]].max()))

    plt.show()


def resize_images(images):
    return [transform.resize(image, (28, 28)) for image in images]


def load_reshaped_dataset(data_directory):
    images, labels = load_data(data_directory)
    images = resize_images(images)
    images = rgb2gray(np.array(images))
    return images, np.asarray(labels, dtype=np.int32)
