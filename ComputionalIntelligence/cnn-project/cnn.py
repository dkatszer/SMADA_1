import tensorflow as tf
import tensorflow.contrib.learn as tf_l
resizing_size = 28
number_of_labels = 62

# Graph defines only operation which will be helded one by one
def model(features, labels, mode):
    # Input Layer
    input_layer = tf.reshape(features["x"],
                             [-1, resizing_size, resizing_size, 1])  # -1 indicates dynamic computed batch size

    # how inputs should looks like = [batch_size, image_height, image_width, channels]
    # where:
    #   image_height = image_width
    #   channels = 1 (gray)

    # --------- Convolutional Layer #1 -------------
    conv1 = tf.layers.conv2d(
        inputs=input_layer,
        filters=32,  # how many feature detectors (filters)
        kernel_size=5,  # filter size 5 by 5(sometimes it can be 3 by 3)
        padding="same",
        # input tensor height and width = output tensor height and width. It adds 0 to the edges to preserve size
        activation=tf.nn.relu
    )  # apply ReLu after all, we don't want to have negative values - it is done in order to break linearity

    # --------- Pooling Layer #1 -------------
    pool1 = tf.layers.max_pooling2d(
        inputs=conv1,
        pool_size=[2, 2],  # just like in udemy
        strides=2)  # how many pixels we are moving - in this case I choose 2 in order to not overlap during max_pooling

    # --------- Convolutional Layer #2 and Pooling Layer #2 -------------
    # By applying it again we reduce the size of images by 50% again (because of pooling)
    conv2 = tf.layers.conv2d(
        inputs=pool1,
        filters=64,
        kernel_size=5,
        padding="same",
        activation=tf.nn.relu)
    pool2 = tf.layers.max_pooling2d(
        inputs=conv2,
        pool_size=[2, 2],
        strides=2)

    # ------------ Flattening -------------

    pool2_flat = tf.reshape(pool2, [-1, int(resizing_size / 4) * int(resizing_size / 4) * 64])
    # resizing_size/4 - because during every pooling resulting size was smaller by 50%
    # 64 - numbers of filters
    # for resizing_size = 28 it should have size equals [batch_size, 3136]

    # ------------ Dense Layer -------------

    dense = tf.layers.dense(
        inputs=pool2_flat,
        units=512,
        activation=tf.nn.relu)

    # randomly drop out 40% elements ONLY during training. It is only for helping in training model.
    dropout = tf.layers.dropout(
        inputs=dense,
        rate=0.4,
        training=mode == tf.estimator.ModeKeys.TRAIN)

    # ------------ Logits Layer ------------
    logits = tf.layers.dense(
        inputs=dropout,
        units=number_of_labels)  # how many categories do we have
    # default activation is linear
    # logits = [batch_size, 62]

    predictions = {
        # Generate predictions (for PREDICT and EVAL mode)
        # We want to find the largest value along the dimension with index of 1, which corresponds to our predictions
        # (recall that our logits tensor has shape [batch_size, 62]).
        "classes": tf.argmax(input=logits, axis=1),
        # Add `softmax_tensor` to the graph. It is used for PREDICT and by the
        # It makes that probabilities sum up to 1 !
        "probabilities": tf.nn.softmax(logits, name="softmax_tensor")
    }

    if mode == tf.estimator.ModeKeys.PREDICT:
        return tf.estimator.EstimatorSpec(mode=mode, predictions=predictions)

    # Calculate Loss (for both TRAIN and EVAL=evaluation modes)
    # loss function that measures how closely the model's predictions match the target classes. !!!
    # we are using cross entropy because it is better that avarage square error for classification problem !
    loss = tf.losses.sparse_softmax_cross_entropy(labels=labels, logits=logits)

    # Configure the Training Op (for TRAIN mode)
    # optimize this loss value during training.
    # Use a learning rate of 0.001 and stochastic gradient descent as the optimization algorithm:
    if mode == tf.estimator.ModeKeys.TRAIN:
        optimizer = tf.train.GradientDescentOptimizer(learning_rate=0.002)
        train_op = optimizer.minimize(
            loss=loss,
            global_step=tf.train.get_global_step())
        return tf.estimator.EstimatorSpec(mode=mode, loss=loss, train_op=train_op)

    # Add evaluation metrics (for EVAL mode)
    eval_metric_ops = {
        "accuracy": tf.metrics.accuracy(
            labels=labels, predictions=predictions["classes"])}
    return tf.estimator.EstimatorSpec(
        mode=mode, loss=loss, eval_metric_ops=eval_metric_ops)
