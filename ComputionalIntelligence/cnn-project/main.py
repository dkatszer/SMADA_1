import tensorflow as tf
import tensorflow.contrib.learn as tf_l
import numpy as np
import cnn
import traffic_data

# def main():

tf.logging.set_verbosity(tf.logging.INFO)

# mnist = tf_l.datasets.load_dataset("mnist")
# train_data = mnist.train.images  # Returns np.array
# train_labels = np.asarray(mnist.train.labels, dtype=np.int32)
# eval_data = mnist.test.images  # Returns np.array
# eval_labels = np.asarray(mnist.test.labels, dtype=np.int32)

# Load training and eval data
train_data, train_labels = traffic_data.load_reshaped_dataset(traffic_data.train_data_directory)
eval_data, eval_labels = traffic_data.load_reshaped_dataset(traffic_data.test_data_directory)


# Create the Estimator
classifier = tf.estimator.Estimator(
    model_fn=cnn.model,
    model_dir="tmp/convnet_model")  # it is temp directory for checkpoints

# Set up LOGGING for predictions
tensors_to_log = {"probabilities": "softmax_tensor"}
logging_hook = tf.train.LoggingTensorHook(
    tensors=tensors_to_log, every_n_iter=100)  # probabilities should be logged after every 50 steps of training.

# TRAINING PARAMETERS
train_input_fn = tf.estimator.inputs.numpy_input_fn(
    x={"x": train_data},
    y=train_labels,
    batch_size=100,
    num_epochs=None,
    shuffle=True)
# TRAIN
classifier.train(
    input_fn=train_input_fn,
    steps=10000,
    hooks=[logging_hook])

# EVALUATE PARAMETERS
eval_input_fn = tf.estimator.inputs.numpy_input_fn(
    x={"x": eval_data},
    y=eval_labels,
    num_epochs=1,
    shuffle=False)
# EVALUATE
eval_results = classifier.evaluate(
    input_fn=eval_input_fn)

print(eval_results)
